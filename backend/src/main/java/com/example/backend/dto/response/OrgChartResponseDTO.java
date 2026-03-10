package com.example.backend.dto.response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrgChartResponseDTO {
    private List<OrganizationalHierarchyDTO> managerChain;
    private List<EmpDirectReportDTO> directReports;

}
