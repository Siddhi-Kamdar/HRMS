package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "referral_statuses")
@Getter
@Setter
public class ReferralStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referral_status_id")
    private Long referralStatusId;

    @Column(name = "referral_status_name")
    private String referralStatusName;

}
