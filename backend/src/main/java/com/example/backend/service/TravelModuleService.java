package com.example.backend.service;

import com.example.backend.entity.Travel;
import com.example.backend.repository.TravelModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelModuleService {
    @Autowired
    private TravelModuleRepository travelModuleRepository;

    public List<Travel> findAllTravelDetails(){
        return  travelModuleRepository.findAllTravelDetails();
    }
}
