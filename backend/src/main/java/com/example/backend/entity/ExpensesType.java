package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "expenses_type")
@Getter
@Setter
public class ExpensesType {

    @Id
    @Column(name = "expense_type_id", nullable = false)
    private int expenseId;

    @Column(name = "expence_type_name", nullable = false, length = 50)
    private String expenceTypeName;
}