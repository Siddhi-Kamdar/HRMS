package com.example.backend.controller;

import com.example.backend.dto.request.TravelRequestDTO;
import com.example.backend.dto.response.TravelResponseDTO;
import com.example.backend.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel")
public class TravelController {

    @Autowired
    private TravelService travelService;

    // GET ALL
    @PreAuthorize("hasAnyAuthority('ROLE_HR','ROLE_MANAGER','ROLE_EMPLOYEE')")

    @GetMapping
    public ResponseEntity<List<TravelResponseDTO>> getAllTravels() {
        return ResponseEntity.ok(travelService.getAllTravels());
    }

    // GET BY ID
    @PreAuthorize("hasAnyAuthority('ROLE_HR','ROLE_MANAGER','ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<TravelResponseDTO> getTravelById(@PathVariable Long id) {
        return ResponseEntity.ok(travelService.getTravelById(id));
    }

    // CREATE
    @PreAuthorize("hasRole('HR')")
    @PostMapping
    public ResponseEntity<TravelResponseDTO> createTravel(
            @RequestBody TravelRequestDTO request) {

        return ResponseEntity.ok(travelService.createTravel(request));
    }

    // DELETE
    @PreAuthorize("hasRole('HR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTravel(@PathVariable Long id) {
        travelService.deleteTravel(id);
        return ResponseEntity.ok("Travel deleted successfully");
    }
}