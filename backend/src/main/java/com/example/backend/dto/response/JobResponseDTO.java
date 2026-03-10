package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class JobResponseDTO {
    private int jobId;
    private String jobTitle;
    private String jobSummary;
    private String jobDescriptionUrl;
    private String jobStatus;
    private String postedByName;
    private Date postedDate;

    public JobResponseDTO(int jobId, String jobTitle, String jobSummary, String jobDescriptionUrl, String jobStatus, Date postedDate) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobSummary = jobSummary;
        this.jobDescriptionUrl = jobDescriptionUrl;
        this.jobStatus = jobStatus;
        this.postedDate = postedDate;
    }
}
