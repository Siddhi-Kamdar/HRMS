package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeSimpleDTO {

    private int employeeId;
    private String fullName;
    private Date dateOfBirth;
}
