CREATE OR ALTER PROCEDURE pr_gamemod_apply_for_slot
    @slot_id INT,
    @leader_emp_id INT,
    @members TeamMemberTableType READONLY
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        -----------------------------------------------------
        -- slot validate 
        -----------------------------------------------------

        DECLARE @slot_date DATE;
        DECLARE @start_time TIME;
        DECLARE @game_id INT;
        DECLARE @cycle_id INT;
        DECLARE @priority_score INT;
        DECLARE @existing_booking_id INT;
        DECLARE @existing_priority INT;

        SELECT 
            @slot_date = gs.slot_date,
            @start_time = gst.start_time,
            @game_id = g.game_id
        FROM game_slots gs
        JOIN game_slot_templates gst ON gs.template_id = gst.template_id
        JOIN games g ON gst.game_id = g.game_id
        WHERE gs.slot_id = @slot_id;

        IF @slot_date IS NULL
            THROW 50001, 'Invalid slot.', 1;

        -----------------------------------------------------
        -- time validation
        -----------------------------------------------------

        DECLARE @slot_datetime DATETIME;
        DECLARE @minutes_remaining INT;

        SET @slot_datetime =
            CAST(@slot_date AS DATETIME)
            + CAST(@start_time AS DATETIME);

        SET @minutes_remaining =
            DATEDIFF(MINUTE, GETDATE(), @slot_datetime);

        IF @minutes_remaining <= 0
            THROW 50002, 'Game already started.', 1;

        -----------------------------------------------------
        -- leader validation
        -----------------------------------------------------

        IF NOT EXISTS (SELECT 1 FROM @members WHERE emp_id = @leader_emp_id)
            THROW 50003, 'Leader must be part of team.', 1;

        -----------------------------------------------------
        -- duplicate member validation
        -----------------------------------------------------

        IF EXISTS (
            SELECT emp_id
            FROM @members
            GROUP BY emp_id
            HAVING COUNT(*) > 1
        )
            THROW 50004, 'Duplicate members in team.', 1;

        -----------------------------------------------------
        -- team size validate
        -----------------------------------------------------

        DECLARE @team_size INT;

        SELECT @team_size = COUNT(*) FROM @members;

        IF NOT EXISTS (
            SELECT 1
            FROM game_allowed_configurations
            WHERE game_id = @game_id
              AND allowed_player_count = @team_size
              AND is_active = 1
        )
            THROW 50005, 'Invalid team size.', 1;

        -----------------------------------------------------
        -- double booking validation
        -----------------------------------------------------

        IF EXISTS (
            SELECT 1
            FROM booking_master bm
            JOIN booking_members bmem ON bm.booking_id = bmem.booking_id
            JOIN @members m ON bmem.emp_id = m.emp_id
            JOIN game_slots gs2 ON bm.slot_id = gs2.slot_id
            JOIN game_slot_templates gst2 ON gs2.template_id = gst2.template_id
            WHERE bm.status = 'CONFIRMED'
              AND gs2.slot_date = @slot_date
              AND gst2.start_time = @start_time
        )
            THROW 50006, 'One or more players already booked for another slot at this time.', 1;

        -----------------------------------------------------
        -- penalty check
        -----------------------------------------------------

        IF EXISTS (
            SELECT 1
            FROM employee_penalties ep
            JOIN @members m ON ep.emp_id = m.emp_id
            WHERE ep.restricted_until > GETDATE()
        )
            THROW 50007, 'One or more members restricted.', 1;

        -----------------------------------------------------
        -- cycle check
        -----------------------------------------------------

        SELECT @cycle_id = cycle_id
        FROM game_cycles
        WHERE game_id = @game_id
          AND is_active = 1;

        IF @cycle_id IS NULL
        BEGIN
            INSERT INTO game_cycles (game_id, is_active, started_at)
            VALUES (@game_id, 1, GETDATE());

            SET @cycle_id = SCOPE_IDENTITY();
        END

        -----------------------------------------------------
        -- cycle participation
        -----------------------------------------------------

        INSERT INTO cycle_participation (cycle_id, game_id, emp_id, has_played)
        SELECT @cycle_id, @game_id, m.emp_id, 0
        FROM @members m
        WHERE NOT EXISTS (
            SELECT 1
            FROM cycle_participation cp
            WHERE cp.cycle_id = @cycle_id
              AND cp.emp_id = m.emp_id
        );

        -----------------------------------------------------
        -- priority score calculate
        -----------------------------------------------------

        SELECT @priority_score = COUNT(*)
        FROM cycle_participation cp
        JOIN @members m ON cp.emp_id = m.emp_id
        WHERE cp.cycle_id = @cycle_id
          AND cp.has_played = 0;

        -----------------------------------------------------
        -- slot status check
        -----------------------------------------------------

        SELECT @existing_booking_id = booking_id,
               @existing_priority = priority_score
        FROM booking_master
        WHERE slot_id = @slot_id
          AND status = 'CONFIRMED';

        IF @existing_booking_id IS NULL
        BEGIN
            -------------------------------------------------
            -- confirm booking
            -------------------------------------------------

            INSERT INTO booking_master
                (slot_id, leader_emp_id, cycle_id, team_size, priority_score, status, created_at)
            VALUES
                (@slot_id, @leader_emp_id, @cycle_id, @team_size, @priority_score, 'CONFIRMED', GETDATE());

            DECLARE @new_booking_id INT = SCOPE_IDENTITY();

            INSERT INTO booking_members (booking_id, emp_id)
            SELECT @new_booking_id, emp_id
            FROM @members;

            UPDATE game_slots
            SET status = 'BOOKED'
            WHERE slot_id = @slot_id;
        END
        ELSE
        BEGIN
            -------------------------------------------------
            -- preemption or queue
            -------------------------------------------------

            IF @priority_score > @existing_priority
               AND @minutes_remaining > 10
            BEGIN
                -- mark existing as preempted
                UPDATE booking_master
                SET status = 'PREEMPTED'
                WHERE booking_id = @existing_booking_id;

                -- new confirmed booking
                INSERT INTO booking_master
                    (slot_id, leader_emp_id, cycle_id, team_size, priority_score, status, created_at)
                VALUES
                    (@slot_id, @leader_emp_id, @cycle_id, @team_size, @priority_score, 'CONFIRMED', GETDATE());

                DECLARE @new_booking_id2 INT = SCOPE_IDENTITY();

                INSERT INTO booking_members (booking_id, emp_id)
                SELECT @new_booking_id2, emp_id
                FROM @members;
            END
            ELSE
            BEGIN
                -- insert into queue
                INSERT INTO booking_queue
                    (slot_id, leader_emp_id, cycle_id, team_size, priority_score, status, created_at)
                VALUES
                    (@slot_id, @leader_emp_id, @cycle_id, @team_size, @priority_score, 'WAITING', GETDATE());

                DECLARE @queue_id INT = SCOPE_IDENTITY();

                INSERT INTO booking_queue_members (queue_id, emp_id)
                SELECT @queue_id, emp_id
                FROM @members;
            END
        END

        COMMIT TRANSACTION;
        PRINT 'Application processed successfully.';

    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END

CREATE OR ALTER PROCEDURE pr_gamemod_cancel_booking
    @slot_id INT,
    @cancelled_by_emp_id INT
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        -----------------------------------------------------
        -- slot + booking validation
        -----------------------------------------------------

        DECLARE @slot_date DATE;
        DECLARE @start_time TIME;
        DECLARE @slot_datetime DATETIME;
        DECLARE @minutes_remaining INT;
        DECLARE @booking_id INT;

        SELECT
            @slot_date = gs.slot_date,
            @start_time = gst.start_time
        FROM game_slots gs
        JOIN game_slot_templates gst ON gs.template_id = gst.template_id
        WHERE gs.slot_id = @slot_id;

        IF @slot_date IS NULL
            THROW 60001, 'Invalid slot.', 1;

        SET @slot_datetime =
            CAST(@slot_date AS DATETIME)
            + CAST(@start_time AS DATETIME);

        SET @minutes_remaining =
            DATEDIFF(MINUTE, GETDATE(), @slot_datetime);

        IF @minutes_remaining <= 0
            THROW 60002, 'Game already started. Cannot cancel.', 1;

        IF @minutes_remaining <= 10
            THROW 60003, 'Cannot cancel within last 10 minutes.', 1;

        -----------------------------------------------------
        -- get confirmed booking
        -----------------------------------------------------

        SELECT @booking_id = booking_id
        FROM booking_master
        WHERE slot_id = @slot_id
          AND status = 'CONFIRMED';

        IF @booking_id IS NULL
            THROW 60004, 'No confirmed booking found for slot.', 1;

        -----------------------------------------------------
        -- cancel booking
        -----------------------------------------------------

        UPDATE booking_master
        SET status = 'CANCELED'
        WHERE booking_id = @booking_id;

        UPDATE game_slots
        SET status = 'OPEN'
        WHERE slot_id = @slot_id;

        -----------------------------------------------------
        -- promote from queue (if any)
        -----------------------------------------------------

        DECLARE @queue_id INT;
        DECLARE @leader_emp_id INT;
        DECLARE @cycle_id INT;
        DECLARE @team_size INT;
        DECLARE @priority_score INT;

        SELECT TOP 1
            @queue_id = queue_id,
            @leader_emp_id = leader_emp_id,
            @cycle_id = cycle_id,
            @team_size = team_size,
            @priority_score = priority_score
        FROM booking_queue
        WHERE slot_id = @slot_id
          AND status = 'WAITING'
        ORDER BY priority_score DESC, created_at ASC;

        IF @queue_id IS NOT NULL
        BEGIN
            -------------------------------------------------
            -- confirm promoted booking
            -------------------------------------------------

            INSERT INTO booking_master
                (slot_id, leader_emp_id, cycle_id, team_size, priority_score, status, created_at)
            VALUES
                (@slot_id, @leader_emp_id, @cycle_id, @team_size, @priority_score, 'CONFIRMED', GETDATE());

            DECLARE @new_booking_id INT = SCOPE_IDENTITY();

            INSERT INTO booking_members (booking_id, emp_id)
            SELECT @new_booking_id, emp_id
            FROM booking_queue_members
            WHERE queue_id = @queue_id;

            UPDATE booking_queue
            SET status = 'PROMOTED'
            WHERE queue_id = @queue_id;

            UPDATE game_slots
            SET status = 'BOOKED'
            WHERE slot_id = @slot_id;
        END

        COMMIT TRANSACTION;
        PRINT 'Booking cancelled successfully.';

    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH;
END

CREATE OR ALTER PROCEDURE pr_gamemod_complete_slot
    @slot_id INT
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        -----------------------------------------------------
        -- slot validation
        -----------------------------------------------------

        DECLARE @booking_id INT;
        DECLARE @cycle_id INT;

        IF NOT EXISTS (
            SELECT 1
            FROM game_slots
            WHERE slot_id = @slot_id
        )
            THROW 70001, 'Invalid slot.', 1;

        SELECT @booking_id = booking_id,
               @cycle_id = cycle_id
        FROM booking_master
        WHERE slot_id = @slot_id
          AND status = 'CONFIRMED';

        IF @booking_id IS NULL
            THROW 70002, 'No confirmed booking found.', 1;

        -----------------------------------------------------
        -- mark slot completed
        -----------------------------------------------------

        UPDATE game_slots
        SET status = 'COMPLETED'
        WHERE slot_id = @slot_id;

        UPDATE booking_master
        SET status = 'COMPLETED'
        WHERE booking_id = @booking_id;

        -----------------------------------------------------
        -- update cycle participation (fairness logic)
        -----------------------------------------------------

        UPDATE cp
        SET cp.has_played = 1
        FROM cycle_participation cp
        JOIN booking_members bm ON cp.emp_id = bm.emp_id
        WHERE bm.booking_id = @booking_id
          AND cp.cycle_id = @cycle_id;

        -----------------------------------------------------
        -- reject remaining queue entries
        -----------------------------------------------------

        UPDATE booking_queue
        SET status = 'REJECTED'
        WHERE slot_id = @slot_id
          AND status = 'WAITING';

        COMMIT TRANSACTION;
        PRINT 'Slot marked as completed successfully.';

    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH;
END

CREATE OR ALTER PROCEDURE pr_gamemod_get_games
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        game_id,
        game_name
    FROM games
    WHERE is_active = 1;
END

CREATE OR ALTER PROCEDURE pr_gamemod_get_available_slots
    @game_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        gs.slot_id,
        g.game_name,
        gs.slot_date,
        gst.start_time,
        gst.end_time,
        gs.status
    FROM game_slots gs
    JOIN game_slot_templates gst ON gs.template_id = gst.template_id
    JOIN games g ON gst.game_id = g.game_id
    WHERE g.game_id = @game_id
      AND gs.slot_date >= CAST(GETDATE() AS DATE)
    ORDER BY gs.slot_date, gst.start_time;
END

CREATE OR ALTER PROCEDURE pr_gamemod_get_slot_details
    @slot_id INT
AS
BEGIN
    SET NOCOUNT ON;

    -----------------------------------------------------
    -- slot info
    -----------------------------------------------------
    SELECT
        gs.slot_id,
        gs.slot_date,
        gst.start_time,
        gst.end_time,
        gs.status
    FROM game_slots gs
    JOIN game_slot_templates gst ON gs.template_id = gst.template_id
    WHERE gs.slot_id = @slot_id;

    -----------------------------------------------------
    -- confirmed booking
    -----------------------------------------------------
    SELECT
        bm.booking_id,
        bm.leader_emp_id,
        bm.priority_score,
        bm.status
    FROM booking_master bm
    WHERE bm.slot_id = @slot_id
      AND bm.status = 'CONFIRMED';

    -----------------------------------------------------
    -- confirmed members
    -----------------------------------------------------
    SELECT
        bmem.emp_id
    FROM booking_members bmem
    JOIN booking_master bm ON bmem.booking_id = bm.booking_id
    WHERE bm.slot_id = @slot_id
      AND bm.status = 'CONFIRMED';

    -----------------------------------------------------
    -- queue list
    -----------------------------------------------------
    SELECT
        queue_id,
        leader_emp_id,
        priority_score,
        status,
        created_at
    FROM booking_queue
    WHERE slot_id = @slot_id
      AND status = 'WAITING'
    ORDER BY priority_score DESC, created_at ASC;
END

CREATE OR ALTER PROCEDURE GetMyBookings
    @emp_id INT
AS
BEGIN
    SET NOCOUNT ON;

    -----------------------------------------------------
    -- confirmed & past bookings
    -----------------------------------------------------
    SELECT
        bm.booking_id,
        gs.slot_date,
        gst.start_time,
        bm.status,
        bm.priority_score
    FROM booking_master bm
    JOIN booking_members bmem ON bm.booking_id = bmem.booking_id
    JOIN game_slots gs ON bm.slot_id = gs.slot_id
    JOIN game_slot_templates gst ON gs.template_id = gst.template_id
    WHERE bmem.emp_id = @emp_id
    ORDER BY gs.slot_date DESC;

    -----------------------------------------------------
    -- queue entries
    -----------------------------------------------------
    SELECT
        bq.queue_id,
        gs.slot_date,
        gst.start_time,
        bq.status,
        bq.priority_score
    FROM booking_queue bq
    JOIN booking_queue_members bqm ON bq.queue_id = bqm.queue_id
    JOIN game_slots gs ON bq.slot_id = gs.slot_id
    JOIN game_slot_templates gst ON gs.template_id = gst.template_id
    WHERE bqm.emp_id = @emp_id
      AND bq.status = 'WAITING'
    ORDER BY gs.slot_date;
END

CREATE OR ALTER PROCEDURE GetQueuePosition
    @queue_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        queue_id,
        ROW_NUMBER() OVER (
            ORDER BY priority_score DESC, created_at ASC
        ) AS queue_position
    FROM booking_queue
    WHERE slot_id = (
        SELECT slot_id FROM booking_queue WHERE queue_id = @queue_id
    )
      AND status = 'WAITING';
END
