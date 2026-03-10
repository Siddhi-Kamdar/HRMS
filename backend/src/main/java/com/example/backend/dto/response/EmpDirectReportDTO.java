package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpDirectReportDTO {
    private Integer employeeId;
    private String name;
    private String designation;
    private String profilePicture;

}
