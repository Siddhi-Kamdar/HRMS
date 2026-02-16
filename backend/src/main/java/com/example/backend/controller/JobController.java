
package com.example.backend.controller;

import com.example.backend.dto.response.JobResponseDTO;
import com.example.backend.entity.Job;
import com.example.backend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public JobResponseDTO createJob(
            @RequestBody Job job,
            @RequestParam Long postedById
    ) {
        return jobService.createJob(job, postedById);
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{jobId}")
    public JobResponseDTO updateJob(
            @PathVariable Long jobId,
            @RequestBody Job job
    ) {
        return jobService.updateJob(jobId, job);
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
}