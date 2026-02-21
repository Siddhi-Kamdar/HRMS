package com.example.backend.service;

import com.example.backend.entity.Employee;
import com.example.backend.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoggedInUserService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee getLoggedInEmployee(){

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return employeeRepository
                .findByEmail(email)
                .orElseThrow();
    }
}