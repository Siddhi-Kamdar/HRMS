package com.example.backend.entity;

import com.example.backend.entity.ExpensesType;
import com.example.backend.entity.Travel;
import jakarta.persistence.*;

@Entity
@Table(name = "expenses_detail")
public class ExpensesDetail {
    @Id
    @JoinColumn(name = "expense_id", nullable = false)
    private int expenseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_type_id", nullable = false)
    private ExpensesType expenseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @Column(nullable = false, length = 18)
    private String amount;

    @Column(length = 400)
    private String comment;

    @Column(name = "proof_url", nullable = false, length = 300)
    private String proofUrl;

    @Column(name = "upload_date", nullable = false)
    private String uploadDate;

    @Column(name = "expense_date", nullable = false)
    private String expenseDate;

    @Column(name = "is_deleted", nullable = false)
    private String isDeleted;

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public ExpensesType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpensesType expenseType) {
        this.expenseType = expenseType;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProofUrl() {
        return proofUrl;
    }

    public void setProofUrl(String proofUrl) {
        this.proofUrl = proofUrl;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}