package com.example.backend.service;

import com.example.backend.dto.response.OrgChartResponseDTO;
import com.example.backend.repository.OrganizationalHierarchyRepository;
import org.springframework.stereotype.Service;


@Service
public class OrganizationalHierarchyService {
    public final OrganizationalHierarchyRepository repository;

    public OrganizationalHierarchyService(OrganizationalHierarchyRepository repository){
        this.repository = repository;
    }

    public OrgChartResponseDTO getHierarchy(int empId){
        return repository.getHierarchy(empId);
    }
}
