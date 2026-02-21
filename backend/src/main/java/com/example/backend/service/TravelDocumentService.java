
package com.example.backend.service;

import com.example.backend.dto.response.TravelDocumentResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TravelDocumentService {

    @Value("${file.upload.base-dir}")
    private String baseDir;

    @Autowired
    private TravelDocumentRepository travelDocumentRepository;

    @Autowired
    private TravelModuleRepository travelRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;


    public void uploadDocument(
            MultipartFile file,
            int travelId,
            int uploadedById,
            Integer employeeId,
            int documentTypeId
    ) {

        try {

            String folderPath =
                    baseDir + File.separator + "travel-documents";

            File directory = new File(folderPath);

            if (!directory.exists())
                directory.mkdirs();


            String fileName =
                    UUID.randomUUID() + "_" +
                            file.getOriginalFilename();

            String fullPath =
                    folderPath + File.separator + fileName;

            file.transferTo(new File(fullPath));


            Travel travel = travelRepository.findById((long) travelId)
                    .orElseThrow(() ->
                            new RuntimeException("Travel not found"));

            Employee uploadedBy =
                    employeeRepository.findById((long) uploadedById)
                            .orElseThrow(() ->
                                    new RuntimeException("Uploader not found"));

            DocumentType documentType =
                    documentTypeRepository.findById(documentTypeId)
                            .orElseThrow(() ->
                                    new RuntimeException("Document type not found"));


            String dbPath =
                    "uploads/travel-documents/" + fileName;


            TravelDocument document = new TravelDocument();
            document.setTravel(travel);
            document.setUploadedBy(uploadedBy);
            document.setDocumentType(documentType);
            document.setDocumentUrl(dbPath);
            document.setUploadedDate(new Date());
            document.setDeleted(false);


            if (employeeId != null) {

                Employee employee =
                        employeeRepository.findById((long) employeeId)
                                .orElseThrow(() ->
                                        new RuntimeException("Employee not found"));

                document.setEmployee(employee);
            }

            travelDocumentRepository.save(document);

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "File upload failed : " + e.getMessage());
        }
    }


    public List<TravelDocumentResponseDTO>
    getDocumentsByTravel(int travelId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        String role =
                authentication.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority();

        Employee loggedInUser =
                employeeRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        List<TravelDocument> documents;


        if (role.equals("ROLE_HR")) {

            documents =
                    travelDocumentRepository
                            .findByTravel_TravelId(travelId);
        }

        else if (role.equals("ROLE_MANAGER")) {

            List<Employee> teamMembers =
                    employeeRepository.findBySupervisor(loggedInUser);

            List<Integer> teamIds =
                    teamMembers.stream()
                            .map(Employee::getEmployeeId)
                            .toList();

            documents =
                    travelDocumentRepository
                            .findByTravel_TravelId(travelId)
                            .stream()
                            .filter(doc -> {
                                if (doc.getEmployee() == null)
                                    return true;

                                return teamIds.contains(
                                        doc.getEmployee()
                                                .getEmployeeId());
                            })
                            .toList();
        }

        // -------- EMPLOYEE --------
        else {

            documents =
                    travelDocumentRepository
                            .findByTravel_TravelId(travelId)
                            .stream()
                            .filter(doc -> {

                                if (doc.getEmployee() == null)
                                    return true;

                                return doc.getEmployee()
                                        .getEmployeeId()
                                        ==
                                        loggedInUser.getEmployeeId();
                            })
                            .toList();
        }


        return documents.stream()
                .map(doc ->
                        new TravelDocumentResponseDTO(
                                doc.getId(),
                                doc.getDocumentUrl(),
                                doc.getUploadedDate(),
                                doc.getUploadedBy() != null
                                        ? doc.getUploadedBy().getFullName()
                                        : "System"
                        ))
                .toList();
    }
}