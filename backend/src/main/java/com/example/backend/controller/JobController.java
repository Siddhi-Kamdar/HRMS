
package com.example.backend.controller;

import com.example.backend.dto.response.JobResponseDTO;
import com.example.backend.entity.Job;
import com.example.backend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private JobService jobService;

    // ---------------- GET ALL ----------------
    @GetMapping
    public List<JobResponseDTO> getAllJobs() {
        return jobService.getAllJobs();
    }

    // ---------------- GET BY ID ----------------
    @GetMapping("/{jobId}")
    public JobResponseDTO getJobById(@PathVariable Long jobId) {
        return jobService.getJobById(jobId);
    }

    // ---------------- CREATE ----------------
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasRole('HR')")
    public JobResponseDTO createJob(
            @RequestParam String jobTitle,
            @RequestParam String jobSummary,
            @RequestParam String jobStatus,
            @RequestParam MultipartFile jobDescriptionFile,
            Authentication authentication
    ) {
        return jobService.createJob(
                jobTitle,
                jobSummary,
                jobStatus,
                jobDescriptionFile,
                authentication.getName()
        );
    }

    // ---------------- UPDATE ----------------
    @PutMapping(
            value = "/{jobId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('HR')")
    public JobResponseDTO updateJob(
            @PathVariable Long jobId,
            @RequestParam String jobTitle,
            @RequestParam String jobSummary,
            @RequestParam String jobStatus,
            @RequestParam(required = false) MultipartFile jobDescriptionFile
    ) {
        return jobService.updateJob(
                jobId,
                jobTitle,
                jobSummary,
                jobStatus,
                jobDescriptionFile
        );
    }

    // ---------------- UPDATE STATUS ----------------
    @PatchMapping("/{jobId}/status")
    public JobResponseDTO updateStatus(
            @PathVariable Long jobId,
            @RequestParam String status
    ) {
        return jobService.updateJobStatus(jobId, status);
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{jobId}")
    public void deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
    }


    @PostMapping("/{jobId}/share")
    public void shareJob(
            @PathVariable Long jobId,
            @RequestBody List<String> emails
    ) {
        jobService.shareJob(jobId, emails);
    }
}