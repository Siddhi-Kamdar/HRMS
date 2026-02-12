package com.example.backend.controller;

import com.example.backend.entity.Travel;
import com.example.backend.repository.TravelModuleRepository;
import com.example.backend.service.TravelModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travel")

public class TravelModuleController {
    @Autowired
    private TravelModuleService travelModuleService;

    @GetMapping("/get-all-travels")
    public ResponseEntity<List<Travel>> getAllTravels(){
        List<Travel> travels = travelModuleService.findAllTravelDetails();
        return ResponseEntity.ok(travels);
    }
}
