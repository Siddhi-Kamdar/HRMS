package com.example.backend.controller;

import com.example.backend.dto.response.ReferralResponseDTO;
import com.example.backend.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/api/referrals")
public class ReferralController {

    @Autowired
    private ReferralService referralService;

    @PostMapping("/{jobId}")
    public void referFriend(
            @PathVariable Long jobId,
            @RequestParam String candidateName,
            @RequestParam String candidateEmail,
            @RequestParam(required = false) String shortNote,
            @RequestParam MultipartFile cvFile,
            Authentication authentication
    ) {
        referralService.createReferral(
                jobId,
                candidateName,
                candidateEmail,
                shortNote,
                cvFile,
                authentication.getName()
        );
    }
}