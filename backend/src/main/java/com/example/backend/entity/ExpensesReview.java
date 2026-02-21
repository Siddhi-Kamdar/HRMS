
package com.example.backend.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="expenses_review")
public class ExpensesReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="expense_review_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="expense_id")
    private ExpensesDetail expense;

    @ManyToOne
    @JoinColumn(name="reviewed_by_id")
    private Employee reviewedBy;

    @ManyToOne
    @JoinColumn(name="review_status_id")
    private ApprovalStatus status;

    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    @Column(name="is_deleted")
    private Boolean isDeleted=false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}