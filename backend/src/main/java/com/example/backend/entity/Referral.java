package com.example.backend.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "referrals")
@Getter
@Setter
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refrral_id")
    private Long referralId;

    @ManyToOne
    @JoinColumn(name = "refered_by_id")
    private Employee referredBy;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "candidate_name")
    private String candidateName;

    @Column(name = "candidate_email")
    private String candidateEmail;

    @Column(name = "cv_url")
    private String cvUrl;

    @Column(name = "short_note")
    private String shortNote;
}