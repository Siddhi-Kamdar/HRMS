package com.example.backend.service;

import com.example.backend.dto.response.JobResponseDTO;
import com.example.backend.dto.response.TravelResponseDTO;
import com.example.backend.entity.Job;
import com.example.backend.entity.Travel;
import com.example.backend.repository.JobModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobModuleSevice {
    @Autowired
    private JobModuleRepository jobModuleRepository;

    // ------------ get all ---------------
    public List<JobResponseDTO> getAllJobs() {

        return jobModuleRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ------------ get by id  ---------------
    public JobResponseDTO getJobById(int job_id) {

        Job job = jobModuleRepository.findById((long) job_id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));

        return mapToDTO(job);
    }

    public void deleteJob(int id){
        Job job = jobModuleRepository.findById((long)id).
                orElseThrow(() -> new RuntimeException("Job not found to delete"));
        jobModuleRepository.delete(job);
    }

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
