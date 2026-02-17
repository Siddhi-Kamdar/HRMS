package com.example.backend.service;

import com.example.backend.dto.response.EmployeeSimpleDTO;
import com.example.backend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeSimpleDTO> getAllEmployees() {
        return repository.findAll()
                .stream()
                .map(emp -> new EmployeeSimpleDTO(
                        emp.getEmployeeId(),
                        emp.getFullName()
                ))
                .toList();
    }
}
