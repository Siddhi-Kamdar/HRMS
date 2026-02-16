package com.example.backend.controller;

import com.example.backend.dto.response.ReferralResponseDTO;
import com.example.backend.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referrals")
@CrossOrigin(origins = "*")
public class ReferralController {

    @Autowired
    private ReferralService referralService;

    // ---------------- CREATE ----------------
    @PostMapping
    public ReferralResponseDTO createReferral(
            @RequestParam Long jobId,
            @RequestParam Long referredById,
            @RequestParam String candidateName,
            @RequestParam String candidateEmail,
            @RequestParam String cvUrl,
            @RequestParam(required = false) String shortNote
    ) {
        return referralService.createReferral(
                jobId,
                referredById,
                candidateName,
                candidateEmail,
                cvUrl,
                shortNote
        );
    }

    // ---------------- GET ALL ----------------
    @GetMapping
    public List<ReferralResponseDTO> getAllReferrals() {
        return referralService.getAllReferrals();
    }

    // ---------------- GET BY ID ----------------
    @GetMapping("/{id}")
    public ReferralResponseDTO getById(@PathVariable Long id) {
        return referralService.getReferralById(id);
    }

    // ---------------- UPDATE STATUS ----------------
    @PatchMapping("/{id}/status")
    public ReferralResponseDTO updateStatus(
            @PathVariable Long id,
            @RequestParam Long statusId,
            @RequestParam Long reviewedById
    ) {
        return referralService.updateReferralStatus(id, statusId, reviewedById);
    }
}