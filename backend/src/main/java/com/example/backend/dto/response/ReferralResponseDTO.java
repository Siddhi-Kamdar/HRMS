
package com.example.backend.dto.response;

public class ReferralResponseDTO {

    private Long referralId;
    private String candidateName;
    private String candidateEmail;
    private String cvUrl;
    private String shortNote;

    private String jobTitle;
    private String referredByName;
    private String currentStatus;

    public ReferralResponseDTO(Long referralId,
                               String candidateName,
                               String candidateEmail,
                               String cvUrl,
                               String shortNote,
                               String jobTitle,
                               String referredByName,
                               String currentStatus) {

        this.referralId = referralId;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.cvUrl = cvUrl;
        this.shortNote = shortNote;
        this.jobTitle = jobTitle;
        this.referredByName = referredByName;
        this.currentStatus = currentStatus;
    }

    public Long getReferralId() {
        return referralId;
    }

    public void setReferralId(Long referralId) {
        this.referralId = referralId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getShortNote() {
        return shortNote;
    }

    public void setShortNote(String shortNote) {
        this.shortNote = shortNote;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getReferredByName() {
        return referredByName;
    }

    public void setReferredByName(String referredByName) {
        this.referredByName = referredByName;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}