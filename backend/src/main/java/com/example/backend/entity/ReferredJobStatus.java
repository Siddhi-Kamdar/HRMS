package com.example.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "referred_job_status")
public class ReferredJobStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referred_job_status_id")
    private Long referredJobStatusId;

    @ManyToOne
    @JoinColumn(name = "referral_id")
    private Referral referral;

    @ManyToOne
    @JoinColumn(name = "referral_status_id")
    private ReferralStatus referralStatus;

    @ManyToOne
    @JoinColumn(name = "referral_reviewed_by")
    private Employee reviewedBy;

    @Column(name = "timestamp")
    private java.util.Date timeStamp;

    public Long getReferredJobStatusId() {
        return referredJobStatusId;
    }

    public void setReferredJobStatusId(Long referredJobStatusId) {
        this.referredJobStatusId = referredJobStatusId;
    }

    public Referral getReferral() {
        return referral;
    }

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    public ReferralStatus getReferralStatus() {
        return referralStatus;
    }

    public void setReferralStatus(ReferralStatus referralStatus) {
        this.referralStatus = referralStatus;
    }

    public Employee getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Employee reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
