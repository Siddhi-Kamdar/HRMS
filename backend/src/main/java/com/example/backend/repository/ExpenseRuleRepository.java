package com.example.backend.repository;

import com.example.backend.entity.ExpenseRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRuleRepository extends JpaRepository<ExpenseRule, Integer> {

    ExpenseRule findByExpenseTypeExpenseId(Integer expenseTypeId);

}