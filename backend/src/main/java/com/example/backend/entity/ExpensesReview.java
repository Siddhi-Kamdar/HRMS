package com.example.backend.entity;

import com.example.backend.entity.ApprovalStatus;
import com.example.backend.entity.Employee;
import com.example.backend.entity.ExpensesDetail;
import jakarta.persistence.*;

@Entity
@Table(name = "expenses_review")
public class ExpensesReview {
    @Id
    @Column(name = "expense_review_id", nullable = false)
    private int expenseReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private ExpensesDetail expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id", nullable = false)
    private Employee reviewedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_status_id", nullable = false)
    private ApprovalStatus reviewStatus;

    @Column(length = 400)
    private String comment;

    @Column(nullable = false)
    private String timestamp;

    @Column(name = "is_deleted")
    private String isDeleted;

    public int getExpenseReviewId() {
        return expenseReviewId;
    }

    public void setExpenseReviewId(int expenseReviewId) {
        this.expenseReviewId = expenseReviewId;
    }

    public ExpensesDetail getExpense() {
        return expense;
    }

    public void setExpense(ExpensesDetail expense) {
        this.expense = expense;
    }

    public Employee getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Employee reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public ApprovalStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ApprovalStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}