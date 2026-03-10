
package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReferralResponseDTO {

    private Long referralId;
    private String candidateName;
    private String candidateEmail;
    private String cvUrl;
    private String shortNote;

    private String jobTitle;
    private String referredByName;
    private String currentStatus;
}