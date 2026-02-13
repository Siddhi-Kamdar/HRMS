package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "approval_statuses")
public class ApprovalStatus{
    @Id
    @Column(name = "approval_status_id", nullable = false)
    private int approval_status_id;

    @Column(name = "approval_status_name", nullable = false, length = 50)
    private String approvalStatusName;

    public int getApproval_status_id() {
        return approval_status_id;
    }

    public void setApproval_status_id(int approval_status_id) {
        this.approval_status_id = approval_status_id;
    }

    public String getApprovalStatusName() {
        return approvalStatusName;
    }

    public void setApprovalStatusName(String approvalStatusName) {
        this.approvalStatusName = approvalStatusName;
    }
}