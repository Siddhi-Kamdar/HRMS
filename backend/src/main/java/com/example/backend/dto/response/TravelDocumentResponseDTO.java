package com.example.backend.dto.response;

import jakarta.annotation.Nullable;

import java.util.Date;

public class TravelDocumentResponseDTO {

    private int id;
    private String documentUrl;
    private Date uploadedDate;
    private String uploadedByName;
    @Nullable
//    private int employeeId;
//    private int uploadedById;

    public TravelDocumentResponseDTO(
            int id,
            String documentUrl,
            Date uploadedDate,
            String uploadedByName
//            ,@Nullable int employeeId,
//            int uploadedById
    ) {
        this.id = id;
        this.documentUrl = documentUrl;
        this.uploadedDate = uploadedDate;
        this.uploadedByName = uploadedByName;
//        this.employeeId = employeeId;
//        this.uploadedById = uploadedById;
    }

//    public int getEmployeeId() {
//        return employeeId;
//    }
//
//    public int getUploadedById() {
//        return uploadedById;
//    }

    public int getId() { return id; }
    public String getDocumentUrl() { return documentUrl; }
    public Date getUploadedDate() { return uploadedDate; }
    public String getUploadedByName() { return uploadedByName; }
}
