package com.example.backend.repository;

import com.example.backend.entity.Referral;
import com.example.backend.entity.ReferredJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferredJobStatusRepository extends JpaRepository<ReferredJobStatus, Long> {

    ReferredJobStatus findTopByReferralOrderByTimeStampDesc(Referral referral);
}