package com.example.backend.controller;

import com.example.backend.dto.response.TravelDocumentResponseDTO;
import com.example.backend.entity.TravelDocument;
import com.example.backend.service.TravelDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/travel-documents")
public class TravelDocumentController {

    @Autowired
    private TravelDocumentService travelDocumentService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )

    @PreAuthorize("hasAnyAuthority('ROLE_HR','ROLE_MANAGER','ROLE_EMPLOYEE')")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("travelId") Integer travelId,
            @RequestParam("uploadedById") Integer uploadedById,
            @RequestParam(value = "employeeId", required = false) Integer employeeId,
            @RequestParam("documentTypeId") Integer documentTypeId
    ) {
        travelDocumentService.uploadDocument(file,travelId,uploadedById,employeeId,documentTypeId);
        return ResponseEntity.ok("Document uploaded successfully");
    }


    @PreAuthorize("hasAnyAuthority('ROLE_HR','ROLE_MANAGER','ROLE_EMPLOYEE')")
    @GetMapping("/{travelId}")
    public ResponseEntity<List<TravelDocumentResponseDTO>> getDocuments(
            @PathVariable int travelId) {

        return ResponseEntity.ok(
                travelDocumentService.getDocumentsByTravel(travelId)
        );
    }

}