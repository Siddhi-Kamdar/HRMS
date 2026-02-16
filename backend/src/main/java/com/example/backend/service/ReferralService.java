package com.example.backend.service;

import com.example.backend.dto.response.ReferralResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReferralService {

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private JobModuleRepository jobRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReferralStatusRepository referralStatusRepository;

    @Autowired
    private ReferredJobStatusRepository referredJobStatusRepository;

    // ---------------- CREATE REFERRAL ----------------
    public ReferralResponseDTO createReferral(
            Long jobId,
            Long referredById,
            String candidateName,
            String candidateEmail,
            String cvUrl,
            String shortNote
    ) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Employee referredBy = employeeRepository.findById(referredById)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Referral referral = new Referral();
        referral.setJob(job);
        referral.setReferredBy(referredBy);
        referral.setCandidateName(candidateName);
        referral.setCandidateEmail(candidateEmail);
        referral.setCvUrl(cvUrl);
        referral.setShortNote(shortNote);

        Referral savedReferral = referralRepository.save(referral);

        ReferralStatus defaultStatus = referralStatusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default status not found"));

        ReferredJobStatus statusHistory = new ReferredJobStatus();
        statusHistory.setReferral(savedReferral);
        statusHistory.setReferralStatus(defaultStatus);
        statusHistory.setReviewedBy(null);
        statusHistory.setTimeStamp(new Date());

        referredJobStatusRepository.save(statusHistory);

        return mapToDTO(savedReferral, defaultStatus.getReferralStatusName());
    }

    // ---------------- GET ALL ----------------
    public List<ReferralResponseDTO> getAllReferrals() {

        return referralRepository.findAll()
                .stream()
                .map(this::mapWithLatestStatus)
                .toList();
    }

    // ---------------- GET BY ID ----------------
    public ReferralResponseDTO getReferralById(Long referralId) {

        Referral referral = referralRepository.findById(referralId)
                .orElseThrow(() -> new RuntimeException("Referral not found"));

        return mapWithLatestStatus(referral);
    }

    // ---------------- UPDATE STATUS ----------------
    public ReferralResponseDTO updateReferralStatus(
            Long referralId,
            Long statusId,
            Long reviewedById
    ) {

        Referral referral = referralRepository.findById(referralId)
                .orElseThrow(() -> new RuntimeException("Referral not found"));

        ReferralStatus status = referralStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        Employee reviewer = employeeRepository.findById(reviewedById)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        ReferredJobStatus history = new ReferredJobStatus();
        history.setReferral(referral);
        history.setReferralStatus(status);
        history.setReviewedBy(reviewer);
        history.setTimeStamp(new Date());

        referredJobStatusRepository.save(history);

        return mapToDTO(referral, status.getReferralStatusName());
    }

    // ---------------- HELPER METHODS ----------------

    private ReferralResponseDTO mapWithLatestStatus(Referral referral) {

        ReferredJobStatus latest =
                referredJobStatusRepository
                        .findTopByReferralReferralIdOrderByTimeStampDesc(
                                referral.getReferralId()
                        );

        String statusName = (latest != null)
                ? latest.getReferralStatus().getReferralStatusName()
                : "UNKNOWN";

        return mapToDTO(referral, statusName);
    }

    private ReferralResponseDTO mapToDTO(Referral referral, String statusName) {

        return new ReferralResponseDTO(
                referral.getReferralId(),
                referral.getCandidateName(),
                referral.getCandidateEmail(),
                referral.getCvUrl(),
                referral.getShortNote(),
                referral.getJob().getJob_title(),
                referral.getReferredBy().getFullName(),
                statusName
        );
    }
}
