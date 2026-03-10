package com.example.backend.controller;

import com.example.backend.entity.ExpensesType;
import com.example.backend.repository.ExpensesTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense-types")
public class ExpenseTypeController {

    @Autowired
    private ExpensesTypeRepository repo;

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE','HR','MANAGER')")
    public List<ExpensesType> getAll() {
        return repo.findAll();
    }
}