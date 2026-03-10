package com.example.backend.repository;

import com.example.backend.entity.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalStatusRepository extends JpaRepository<ApprovalStatus, Integer> {

    ApprovalStatus findByApprovalStatusName(String approvalStatusName);

}