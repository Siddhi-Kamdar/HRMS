package com.example.backend.service;

import com.example.backend.dto.response.JobResponseDTO;
import com.example.backend.entity.Employee;
import com.example.backend.entity.Job;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.JobModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.Authenticator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobModuleRepository jobRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmailService emailService;

    public List<JobResponseDTO> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public JobResponseDTO getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return mapToDTO(job);
    }


    public JobResponseDTO createJob(
            String jobTitle,
            String jobSummary,
            String jobStatus,
            MultipartFile file,
            String loggedInEmail
    ) {

        Employee employee = employeeRepository
                .findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String uploadDir = "uploads/job-description-files/";
        String fileName = System.currentTimeMillis()
                + "_" + file.getOriginalFilename();

        try {
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("File upload failed");
        }

        Job job = new Job();
        job.setJob_title(jobTitle);
        job.setJob_summary(jobSummary);
        job.setJob_status(jobStatus);
        job.setJob_description_url(uploadDir+fileName);
        job.setPosted_by_employee(employee);
        job.setPosted_date(new Date());

        Job saved = jobRepository.save(job);

        return mapToDTO(saved);
    }

    public JobResponseDTO updateJob(
            Long jobId,
            String jobTitle,
            String jobSummary,
            String jobStatus,
            MultipartFile file
    ) {

        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        existingJob.setJob_title(jobTitle);
        existingJob.setJob_summary(jobSummary);
        existingJob.setJob_status(jobStatus);

        if (file != null && !file.isEmpty()) {

            String uploadDir = "uploads/job-description-files/";
            String fileName = System.currentTimeMillis()
                    + "_" + file.getOriginalFilename();

            try {
                Path path = Paths.get(uploadDir + fileName);
                Files.write(path, file.getBytes());
            } catch (Exception e) {
                throw new RuntimeException("File upload failed");
            }

            existingJob.setJob_description_url(fileName);
        }

        Job saved = jobRepository.save(existingJob);

        return mapToDTO(saved);
    }

    public void deleteJob(Long jobId) {
        Authentication aut = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTH OBJECT" + aut);
        System.out.println("Authorities: "+ aut.getAuthorities() );

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        jobRepository.delete(job);
    }

    public JobResponseDTO updateJobStatus(Long jobId, String status) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setJob_status(status);

        return mapToDTO(jobRepository.save(job));
    }

    public void shareJob(Long jobId, List<String> emails) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String subject = "Job Opportunity: " + job.getJob_title();

        String body = "Job Title: " + job.getJob_title()
                + "\nSummary: " + job.getJob_summary();

        String filePath = job.getJob_description_url();

        for (String email : emails) {
            emailService.sendMailWithAttachment(
                    email,
                    subject,
                    body,
                    filePath
            );
        }
    }
    private JobResponseDTO mapToDTO(Job job) {

        return new JobResponseDTO(
                job.getJob_id(),
                job.getJob_title(),
                job.getJob_summary(),
                job.getJob_description_url(),
                job.getJob_status(),
                job.getPosted_date()
        );
    }


}
