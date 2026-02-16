package com.example.backend.repository;

import com.example.backend.entity.ReferralStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralStatusRepository extends JpaRepository<ReferralStatus, Long> {
}
