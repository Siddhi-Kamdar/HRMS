package com.example.backend.controller;

import com.example.backend.dto.response.OrgChartResponseDTO;
import com.example.backend.dto.response.OrganizationalHierarchyDTO;
import com.example.backend.dto.response.SlotResponseDTO;
import com.example.backend.service.OrganizationalHierarchyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hierarchy")
public class OrganizationalHierarchyController {
    private final OrganizationalHierarchyService service;

    public OrganizationalHierarchyController(OrganizationalHierarchyService service){
        this.service = service;
    }
    @GetMapping("/{empId}/hierarchy")
    public OrgChartResponseDTO getHierarchy(@PathVariable int empId) {
        return service.getHierarchy(empId);
    }
}

