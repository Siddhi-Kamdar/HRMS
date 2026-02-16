package com.example.backend.service;

import com.example.backend.dto.response.JobResponseDTO;
import com.example.backend.entity.Employee;
import com.example.backend.entity.Job;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.JobModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobModuleRepository jobRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // ---------------- GET ALL ----------------
    public List<JobResponseDTO> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ---------------- GET BY ID ----------------
    public JobResponseDTO getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return mapToDTO(job);
    }

    // ---------------- CREATE ----------------
    public JobResponseDTO createJob(Job job, Long postedById) {

        Employee employee = employeeRepository.findById(postedById)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        job.setPosted_by_employee(employee);
        job.setPosted_date(new Date());

        Job savedJob = jobRepository.save(job);

        return mapToDTO(savedJob);
    }

    // ---------------- UPDATE ----------------
    public JobResponseDTO updateJob(Long jobId, Job updatedJob) {

        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        existingJob.setJob_title(updatedJob.getJob_title());
        existingJob.setJob_summary(updatedJob.getJob_summary());
        existingJob.setJob_description_url(updatedJob.getJob_description_url());
        existingJob.setJob_status(updatedJob.getJob_status());

        Job saved = jobRepository.save(existingJob);

        return mapToDTO(saved);
    }

    // ---------------- DELETE ----------------
    public void deleteJob(Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        jobRepository.delete(job);
    }

    // ---------------- CHANGE STATUS ----------------
    public JobResponseDTO updateJobStatus(Long jobId, String status) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setJob_status(status);

        return mapToDTO(jobRepository.save(job));
    }

    // ---------------- DTO MAPPER ----------------
    private JobResponseDTO mapToDTO(Job job) {

        return new JobResponseDTO(
                job.getJob_id(),
                job.getJob_title(),
                job.getJob_summary(),
                job.getJob_description_url(),
                job.getJob_status(),
                job.getPosted_by_employee().getFullName(),
                job.getPosted_date()
        );
    }
}
