package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrganizationalHierarchyDTO {
    private Integer employeeId;
    private String employeeName;
    private String supervisiorName;
    private String employeePosition;
    private String supervisiorPosition ;
    private String employeeProfilePicture;
    private String supervisiorProfilePicture;
}
