package com.example.backend.dto.response;

public class EmployeeSimpleDTO {

    private int employeeId;
    private String fullName;

    public EmployeeSimpleDTO(int employeeId, String fullName) {
        this.employeeId = employeeId;
        this.fullName = fullName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFullName() {
        return fullName;
    }
}
