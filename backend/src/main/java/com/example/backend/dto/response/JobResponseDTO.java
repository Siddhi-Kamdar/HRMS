package com.example.backend.dto.response;

import java.util.Date;

public class JobResponseDTO {
    private int jobId;
    private String jobTitle;
    private String jobSummary;
    private String jobDescriptionUrl;
    private String jobStatus;
    private String postedByName;
    private Date postedDate;

    public JobResponseDTO(int jobId, String jobTitle, String jobSummary, String jobDescriptionUrl, String jobStatus, String postedByName, Date postedDate) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobSummary = jobSummary;
        this.jobDescriptionUrl = jobDescriptionUrl;
        this.jobStatus = jobStatus;
        this.postedByName = postedByName;
        this.postedDate = postedDate;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobSummary() {
        return jobSummary;
    }

    public void setJobSummary(String jobSummary) {
        this.jobSummary = jobSummary;
    }

    public String getJobDescriptionUrl() {
        return jobDescriptionUrl;
    }

    public void setJobDescriptionUrl(String jobDescriptionUrl) {
        this.jobDescriptionUrl = jobDescriptionUrl;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }
}
