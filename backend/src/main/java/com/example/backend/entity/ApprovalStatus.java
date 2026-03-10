package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "approval_statuses")
@Getter
@Setter
public class ApprovalStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_status_id")
    private Integer approvalStatusId;

    @Column(name = "approval_status_name")
    private String approvalStatusName;

}
