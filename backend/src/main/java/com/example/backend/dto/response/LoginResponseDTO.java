package com.example.backend.dto.response;

public class LoginResponseDTO {

    private String token;
    private int employeeId;
    private String fullName;
    private String email;
    private String role;

    public LoginResponseDTO(String token,
                            int employeeId,
                            String fullName,
                            String email,
                            String role) {
        this.token = token;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}