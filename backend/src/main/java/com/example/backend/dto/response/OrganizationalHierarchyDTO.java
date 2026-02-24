package com.example.backend.dto.response;

public class OrganizationalHierarchyDTO {
    private Integer employeeId;
    private String employeeName;
    private String supervisiorName;
    private String employeePosition;
    private String supervisiorPosition ;
    private String employeeProfilePicture;
    private String supervisiorProfilePicture;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public OrganizationalHierarchyDTO(Integer employeeId, String employeeName, String supervisiorName, String employeePosition, String supervisiorPosition, String employeeProfilePicture, String supervisiorProfilePicture) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.supervisiorName = supervisiorName;
        this.employeePosition = employeePosition;
        this.supervisiorPosition = supervisiorPosition;
        this.employeeProfilePicture = employeeProfilePicture;
        this.supervisiorProfilePicture = supervisiorProfilePicture;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSupervisiorName() {
        return supervisiorName;
    }

    public void setSupervisiorName(String supervisiorName) {
        this.supervisiorName = supervisiorName;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public String getSupervisiorPosition() {
        return supervisiorPosition;
    }

    public void setSupervisiorPosition(String supervisiorPosition) {
        this.supervisiorPosition = supervisiorPosition;
    }

    public String getEmployeeProfilePicture() {
        return employeeProfilePicture;
    }

    public void setEmployeeProfilePicture(String employeeProfilePicture) {
        this.employeeProfilePicture = employeeProfilePicture;
    }

    public String getSupervisiorProfilePicture() {
        return supervisiorProfilePicture;
    }

    public void setSupervisiorProfilePicture(String supervisiorProfilePicture) {
        this.supervisiorProfilePicture = supervisiorProfilePicture;
    }
}
