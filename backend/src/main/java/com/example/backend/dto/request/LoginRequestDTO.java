package com.example.backend.dto.request;

public class LoginRequestDTO {

    private String userEmail;
    private String password;

    public LoginRequestDTO() {
    }

    public String getEmail() {
        return userEmail;
    }

    public void setEmail(String email) {
        this.userEmail = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}