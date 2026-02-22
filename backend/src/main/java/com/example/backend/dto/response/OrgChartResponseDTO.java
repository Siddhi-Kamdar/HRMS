package com.example.backend.dto.response;


import java.util.List;

public class OrgChartResponseDTO {
    private List<OrganizationalHierarchyDTO> managerChain;
    private List<EmpDirectReportDTO> directReports;

    public List<OrganizationalHierarchyDTO> getManagerChain() {
        return managerChain;
    }

    public void setManagerChain(List<OrganizationalHierarchyDTO> managerChain) {
        this.managerChain = managerChain;
    }

    public List<EmpDirectReportDTO> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<EmpDirectReportDTO> directReports) {
        this.directReports = directReports;
    }
}
