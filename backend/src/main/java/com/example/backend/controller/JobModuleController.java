package com.example.backend.controller;

import com.example.backend.dto.response.JobResponseDTO;
import com.example.backend.service.JobModuleSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobModuleController {
    @Autowired
    private JobModuleSevice jobModuleSevice;

    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs(){
        return ResponseEntity.ok(jobModuleSevice.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable int id){
        return ResponseEntity.ok(jobModuleSevice.getJobById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable int id){
        jobModuleSevice.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully");
    }
}
