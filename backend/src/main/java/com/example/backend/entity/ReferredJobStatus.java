package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "referred_job_status")
@Getter
@Setter
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
}
