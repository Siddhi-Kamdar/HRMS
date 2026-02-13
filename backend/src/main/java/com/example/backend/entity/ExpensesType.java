package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "expenses_type")
public class ExpensesType {

    @Id
    @Column(name = "expense_type_id", nullable = false)
    private int expenseId;

    @Column(name = "expence_type_name", nullable = false, length = 50)
    private String expenceTypeName;

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenceTypeName() {
        return expenceTypeName;
    }

    public void setExpenceTypeName(String expenceTypeName) {
        this.expenceTypeName = expenceTypeName;
    }
}