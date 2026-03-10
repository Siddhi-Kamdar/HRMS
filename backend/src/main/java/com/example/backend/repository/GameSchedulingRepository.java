package com.example.backend.repository;

import com.example.backend.dto.request.ApplySlotRequestDTO;
import com.example.backend.dto.response.GameResponseDTO;
import com.example.backend.dto.response.SlotDetailDTO;
import com.example.backend.dto.response.SlotResponseDTO;
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

    public List<GameResponseDTO> getGames() {
        return jdbcTemplate.query(
                "EXEC pr_gamemod_get_games",
                (rs, rowNum) ->
                        new GameResponseDTO(
                                rs.getInt("game_id"),
                                rs.getString("game_name")
                        )
        );
    }

    public List<SlotResponseDTO> getAvailableSlots(int gameId, int empId) {
        return jdbcTemplate.query(
                "EXEC pr_gamemod_get_available_slots ?,?",
                new Object[]{gameId, empId},
                (rs, rowNum) ->
                        new SlotResponseDTO(
                                rs.getInt("slot_id"),
                                rs.getString("slot_date"),
                                rs.getString("start_time"),
                                rs.getString("end_time"),
                                rs.getString("status"),
                                rs.getString("booked_by"),
                                rs.getBoolean("is_my_slot")
                        )
        );
    }

    public void applyForSlot(ApplySlotRequestDTO request) {

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

    public void cancelBooking(int slotId, int cancelledByEmpId) {
        jdbcTemplate.update(
                "EXEC pr_gamemod_cancel_booking @slot_id = ?, @cancelled_by_emp_id = ?",
                slotId,
                cancelledByEmpId
        );
    }

    public void completeSlot(int slotId) {
        jdbcTemplate.update(
                "EXEC pr_gamemod_complete_slot @slot_id = ?",
                slotId
        );
    }

    public SlotDetailDTO getSlotDetail(
            int slotId,
            int empId){

        return jdbcTemplate.queryForObject(
                "EXEC pr_gamemod_get_slot_detail ?,?",
                new Object[]{slotId,empId},
                (rs,row)-> new SlotDetailDTO(
                        rs.getString("game_name"),
                        rs.getString("slot_date"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("status"),
                        rs.getString("my_status")
                )
        );
    }

    public void enableSlot(int slotId){
        jdbcTemplate.update(
                "EXEC pr_gamemod_enable_slot @slot_id = ?",
                slotId
        );
    }

    public void disableSlot(int slotId){
        jdbcTemplate.update(
                "EXEC pr_gamemod_disable_slot @slot_id = ?",
                slotId
        );
    }

    public void generateSlots() {
        jdbcTemplate.update("EXEC pr_gamemod_generate_slots");
    }

}