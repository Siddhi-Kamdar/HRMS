package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "referral_statuses")
public class ReferralStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referral_status_id")
    private Long referralStatusId;

    @Column(name = "referral_status_name")
    private String referralStatusName;

    public Long getReferralStatusId() {
        return referralStatusId;
    }

    public void setReferralStatusId(Long referralStatusId) {
        this.referralStatusId = referralStatusId;
    }

    public String getReferralStatusName() {
        return referralStatusName;
    }

    public void setReferralStatusName(String referralStatusName) {
        this.referralStatusName = referralStatusName;
    }
}
