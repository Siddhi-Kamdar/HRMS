package com.example.backend.repository;

import com.example.backend.dto.request.ApplySlotRequest;
import com.example.backend.dto.response.GameResponse;
import com.example.backend.dto.response.SlotResponse;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.List;

@Repository
public class GameSchedulingRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameSchedulingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ---------------- read ----------------

    public List<GameResponse> getGames() {
        return jdbcTemplate.query(
                "EXEC pr_gamemod_get_games",
                (rs, rowNum) ->
                        new GameResponse(
                                rs.getInt("game_id"),
                                rs.getString("game_name")
                        )
        );
    }

    //--------------- get available slots -----------
    public List<SlotResponse> getAvailableSlots(int gameId) {
        return jdbcTemplate.query(
                "EXEC pr_gamemod_get_available_slots @game_id = ?",
                new Object[]{gameId},
                (rs, rowNum) ->
                        new SlotResponse(
                                rs.getInt("slot_id"),
                                rs.getString("slot_date"),
                                rs.getString("start_time"),
                                rs.getString("end_time"),
                                rs.getString("status")
                        )
        );
    }

    // ---------------- apply ----------------

    public void applyForSlot(ApplySlotRequest request) {

        jdbcTemplate.execute((Connection connection) -> {

            SQLServerDataTable tvp = new SQLServerDataTable();
            tvp.addColumnMetadata("emp_id", Types.INTEGER);

            for (Integer memberId : request.getMembers()) {
                tvp.addRow(memberId);
            }
            CallableStatement callableStatements = connection.prepareCall("{call pr_gamemod_apply_for_slot(?, ?, ?)}");
//            SQLServerCallableStatement cs =
//                    (SQLServerCallableStatement)
//                            connection.prepareCall("{call pr_gamemod_apply_for_slot(?, ?, ?)}");
//
            SQLServerCallableStatement cs = callableStatements.unwrap(SQLServerCallableStatement.class);

            cs.setInt(1, request.getSlotId());
            cs.setInt(2, request.getLeaderEmpId());

            cs.setStructured(3, "TeamMemberTableType", tvp);

            cs.execute();

            return null;
        });
    }

    // ---------------- cancel ----------------

    public void cancelBooking(int slotId, int cancelledByEmpId) {
        jdbcTemplate.update(
                "EXEC pr_gamemod_cancel_booking @slot_id = ?, @cancelled_by_emp_id = ?",
                slotId,
                cancelledByEmpId
        );
    }

    // ---------------- complete ----------------

    public void completeSlot(int slotId) {
        jdbcTemplate.update(
                "EXEC pr_gamemod_complete_slot @slot_id = ?",
                slotId
        );
    }

    // ---------------- get booking by emp id ----------------


}