package com.example.backend.repository;

import com.example.backend.entity.ExpensesReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesReviewRepository
        extends JpaRepository<ExpensesReview,Integer> {

    ExpensesReview
    findTopByExpenseExpenseIdOrderByTimestampDesc(Integer expenseId);
}