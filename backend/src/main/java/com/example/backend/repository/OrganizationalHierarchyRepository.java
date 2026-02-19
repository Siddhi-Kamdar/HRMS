package com.example.backend.repository;

import com.example.backend.dto.response.OrganizationalHierarchyDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrganizationalHierarchyRepository {
    private final JdbcTemplate jdbcTemplate;
    public OrganizationalHierarchyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrganizationalHierarchyDTO> getHierarchy(int emp_id){
        return jdbcTemplate.query(
        "EXEC pr_orgmod_get_hierarchy @emp_id = ?",new Object[]{emp_id},
                (rs, rowNumber) ->
                        new OrganizationalHierarchyDTO(
                                rs.getString("employee_name"),
                                rs.getString("supervision_name"),
                                rs.getString("employee_position"),
                                rs.getString("supervisior_position"),
                                rs.getString("employee_pp"),
                                rs.getString("supervisior_pp")
                        )
        );
    }
}
