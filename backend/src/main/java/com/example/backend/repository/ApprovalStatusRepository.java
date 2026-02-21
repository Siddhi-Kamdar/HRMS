package com.example.backend.repository;

import com.example.backend.entity.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalStatusRepository
        extends JpaRepository<ApprovalStatus, Integer> {
}
