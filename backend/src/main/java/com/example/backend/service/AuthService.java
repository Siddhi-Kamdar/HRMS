package com.example.backend.service;

import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.response.LoginResponseDTO;
import com.example.backend.entity.Employee;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.config.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtUtil jwtUtil;


    public LoginResponseDTO login(LoginRequestDTO request) {

        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!employee.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String roleName = employee.getRole().getRoleName();

        String token = jwtUtil.generateToken(employee.getEmail(), roleName);

        return new LoginResponseDTO(
                token,
                employee.getEmployeeId(),
                employee.getFullName(),
                employee.getEmail(),
                roleName
        );
    }

}