package com.example.backend.controller;

import com.example.backend.dto.response.EmployeeSimpleDTO;
import com.example.backend.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmployeeSimpleDTO> getEmployees() {
        return service.getAllEmployees();
    }
}