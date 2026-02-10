declare @team2 TeamMemberTableType
insert into @team2 values(3);
insert into @team2 values(5);
insert into @team2 values(7);
insert into @team2 values(8);

exec ApplyForSlot @slot_id = 1, @leader_emp_id = 3, @members = @team2;

CREATE OR ALTER PROCEDURE ApplyForSlot
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

        IF CAST(GETDATE() AS DATE) > @slot_date
            THROW 50002, 'Slot already passed.', 1;

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
            THROW 50003, 'Invalid team size.', 1;

        -----------------------------------------------------
        -- penalty check
        -----------------------------------------------------

        IF EXISTS (
            SELECT 1
            FROM employee_penalties ep
            JOIN @members m ON ep.emp_id = m.emp_id
            WHERE ep.restricted_until > GETDATE()
        )
            THROW 50004, 'One or more members restricted.', 1;

        -----------------------------------------------------
        -- cycle check make
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
        -- cycle participaton
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
            -- yes-> book
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
            -- no-> preemption
            -------------------------------------------------

            IF @priority_score > @existing_priority
            BEGIN
                -- juni booking udado
                UPDATE booking_master
                SET status = 'PREEMPTED'
                WHERE booking_id = @existing_booking_id;

                -- new booking
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
                THROW 50005, 'Lower priority. Booking rejected.', 1;
            END
        END

        COMMIT TRANSACTION;
        PRINT 'Booking successful.';

    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END

select * from booking_master
select * from booking_members
select * from  game_slots
select * from game_cycles
select * from cycle_participation
select * from game_slot_generation_config

ALTER TABLE game_slots
ADD CONSTRAINT CK_Game_Slot_Status
CHECK (status IN ('OPEN', 'BOOKED', 'COMPLETED', 'CANCLED'))

ALTER TABLE booking_master
ADD CONSTRAINT CK_Booking_Status
CHECK (status IN ('CONFIRMED', 'PREEMPTED', 'CANCLED'))

CREATE PROCEDURE CancleSlot
	@slot_id INT
AS
BEGIN
	SET NOCOUNT ON;
    SET XACT_ABORT ON;
		DECLARE @slot_date DATE;
        DECLARE @start_time TIME;
		DECLARE @emp_id INT;

		SELECT 
            @slot_date = gs.slot_date,
            @start_time = gst.start_time
        FROM game_slots gs
        JOIN game_slot_templates gst ON gs.template_id = gst.template_id
        WHERE gs.slot_id = @slot_id;

		IF @start_time < CONVERT(TIME, GETDATE())
		BEGIN
	    --------------------------------------------------
        -- cancel slot 
        -----------------------------------------------------
			UPDATE game_slots
			SET status = 'OPEN'
			WHERE slot_id = @slot_id
		END
		ELSE
		BEGIN
		--------------------------------------------------
        -- add penalty  
        -----------------------------------------------------
			UPDATE employee_penalties 
			SET late_cancel_count = late_cancel_count + 1
			WHERE emp_id = @emp_id
		END

	BEGIN TRY
	BEGIN TRANSACTION;
		
	COMMIT TRANSACTION;
	END TRY
	BEGIN CATCH
		
	END CATCH;
END

DROP PROCEDURE CancleSlot