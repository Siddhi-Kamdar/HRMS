package com.example.backend.service;

import com.example.backend.dto.response.ReferralDashboardDTO;
import com.example.backend.dto.response.ReferralResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class ReferralService {

    @Autowired
    private JobModuleRepository jobRepository;

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReferralStatusRepository referralStatusRepository;

    @Autowired
    private ReferredJobStatusRepository referredJobStatusRepository;

    public void createReferral(
            Long jobId,
            String candidateName,
            String candidateEmail,
            String shortNote,
            MultipartFile cvFile,
            String loggedInEmail
    ) {
        Employee employee = employeeRepository
                .findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String uploadDir = "uploads/referral-cvs/";
        String fileName = System.currentTimeMillis()
                + "_" + cvFile.getOriginalFilename();

        try {
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, cvFile.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("CV upload failed");
        }

        Referral referral = new Referral();
        referral.setJob(job);
        referral.setCandidateName(candidateName);
        referral.setCandidateEmail(candidateEmail);
        referral.setShortNote(shortNote);
        referral.setReferredBy(employee);
        referral.setCvUrl(fileName);

        referralRepository.save(referral);
        ReferralStatus newStatus = referralStatusRepository
                .findByReferralStatusName("NEW")
                .orElseThrow(() ->
                        new RuntimeException("New status not found in DB"));

        ReferredJobStatus statusEntry = new ReferredJobStatus();
        statusEntry.setReferral(referral);
        statusEntry.setReferralStatus(newStatus);
        statusEntry.setReviewedBy(null);
        statusEntry.setTimeStamp(new Date());

        referredJobStatusRepository.save(statusEntry);

        emailService.sendMailWithAttachment(
                "siddhikamdar1624@gmail.com",
                "New Referral for " + job.getJob_title(),
                "Candidate: " + candidateName,
                uploadDir + fileName
        );
    }
    public void updateStatus(
            Long referralId,
            Long statusId,
            String loggedInEmail
    ) {

        Referral referral = referralRepository.findById(referralId)
                .orElseThrow(() ->
                        new RuntimeException("Referral not found"));

        ReferralStatus status = referralStatusRepository
                .findById(statusId)
                .orElseThrow(() ->
                        new RuntimeException("Status not found"));

        Employee reviewer = employeeRepository
                .findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        ReferredJobStatus statusEntry = new ReferredJobStatus();
        statusEntry.setReferral(referral);
        statusEntry.setReferralStatus(status);
        statusEntry.setReviewedBy(reviewer);
        statusEntry.setTimeStamp(new Date());

        referredJobStatusRepository.save(statusEntry);
    }

    public List<ReferralDashboardDTO> getAllReferrals() {

        List<Referral> referrals = referralRepository.findAll();

        return referrals.stream().map(referral -> {

            ReferredJobStatus latestStatus =
                    referredJobStatusRepository
                            .findTopByReferralOrderByTimeStampDesc(referral);

            String statusName = latestStatus != null
                    ? latestStatus.getReferralStatus().getReferralStatusName()
                    : "Unknown";

            return new ReferralDashboardDTO(
                    referral.getReferralId(),
                    referral.getCandidateName(),
                    referral.getJob().getJob_title(),
                    referral.getReferredBy().getFullName(),
                    statusName
            );

        }).toList();
    }
}