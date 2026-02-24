package com.example.backend.repository;

import com.example.backend.dto.response.EmpDirectReportDTO;
import com.example.backend.dto.response.OrgChartResponseDTO;
import com.example.backend.dto.response.OrganizationalHierarchyDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrganizationalHierarchyRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrganizationalHierarchyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public OrgChartResponseDTO getHierarchy(int empId) {

        SimpleJdbcCall jdbcCall =
                new SimpleJdbcCall(jdbcTemplate)
                        .withProcedureName("pr_orgmod_get_hierarchy");

        Map<String, Object> result =
                jdbcCall.execute(Map.of("emp_id", empId));

        List<Map<String, Object>> managerRows =
                (List<Map<String, Object>>) result.get("#result-set-1");
        System.out.println(managerRows);

        List<Map<String, Object>> reportRows =
                (List<Map<String, Object>>) result.get("#result-set-2");
        System.out.println(reportRows);


        List<OrganizationalHierarchyDTO> managerChain = new ArrayList<>();

        if (managerRows != null) {
            for (Map<String, Object> row : managerRows) {

                managerChain.add(
                        new OrganizationalHierarchyDTO(
                                (Integer) row.get("employee_id"),
                                (String) row.get("employee_name"),
                                (String) row.get("supervision_name"),
                                (String) row.get("employee_position"),
                                (String) row.get("supervisior_position"),
                                (String) row.get("employee_pp"),
                                (String) row.get("supervisior_pp")
                        )
                );
            }
        }


        List<EmpDirectReportDTO> directReports = new ArrayList<>();

        if (reportRows != null) {
            for (Map<String, Object> row : reportRows) {

                EmpDirectReportDTO emp = new EmpDirectReportDTO();
                emp.setEmployeeId(
                        ((Number) row.get("employee_id")).intValue()
                );
                emp.setName((String) row.get("employee_name"));
                emp.setDesignation((String) row.get("employee_position"));
                emp.setProfilePicture((String) row.get("employee_pp"));

                directReports.add(emp);
            }
        }


        OrgChartResponseDTO response = new OrgChartResponseDTO();
        response.setManagerChain(managerChain);
        System.out.println(managerChain);
        response.setDirectReports(directReports);

        return response;
    }
}