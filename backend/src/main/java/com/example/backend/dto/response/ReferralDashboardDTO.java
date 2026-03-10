package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReferralDashboardDTO {

    private Long referralId;
    private String candidateName;
    private String jobTitle;
    private String referredBy;
    private String currentStatus;
}
