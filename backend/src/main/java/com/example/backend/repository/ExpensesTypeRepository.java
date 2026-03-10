package com.example.backend.repository;

import com.example.backend.entity.ExpensesType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensesTypeRepository extends JpaRepository<ExpensesType, Integer> {
}