package com.example.backend.service;

import com.example.backend.dto.response.ReferralResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

        emailService.sendMailWithAttachment(
                "siddhikamdar1624@gmail.com",
                "New Referral for " + job.getJob_title(),
                "Candidate: " + candidateName,
                uploadDir + fileName
        );
    }
}