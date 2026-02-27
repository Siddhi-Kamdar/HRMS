package com.example.backend.dto.response;

public class ReferralDashboardDTO {

    private Long referralId;
    private String candidateName;
    private String jobTitle;
    private String referredBy;
    private String currentStatus;

    public ReferralDashboardDTO(
            Long referralId,
            String candidateName,
            String jobTitle,
            String referredBy,
            String currentStatus
    ) {
        this.referralId = referralId;
        this.candidateName = candidateName;
        this.jobTitle = jobTitle;
        this.referredBy = referredBy;
        this.currentStatus = currentStatus;
    }

    public Long getReferralId() {
        return referralId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }
}
