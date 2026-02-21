package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "approval_statuses")
public class ApprovalStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_status_id")
    private Integer approvalStatusId;

    @Column(name = "approval_status_name")
    private String approvalStatusName;

    public Integer getApprovalStatusId() {
        return approvalStatusId;
    }

    public void setApprovalStatusId(Integer approvalStatusId) {
        this.approvalStatusId = approvalStatusId;
    }

    public String getApprovalStatusName() {
        return approvalStatusName;
    }

    public void setApprovalStatusName(String approvalStatusName) {
        this.approvalStatusName = approvalStatusName;
    }
}
