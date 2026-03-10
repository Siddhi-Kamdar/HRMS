package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_rules")
@Getter
@Setter
public class ExpenseRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_rule_id")
    private Integer expenseRuleId;

    @ManyToOne
    @JoinColumn(name = "expense_type_id")
    private ExpensesType expenseType;

    @Column(name = "max_amount")
    private BigDecimal maxAmount;

}