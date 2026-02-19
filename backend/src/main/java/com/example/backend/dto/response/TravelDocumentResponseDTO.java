package com.example.backend.dto.response;

import java.util.Date;

public class TravelDocumentResponseDTO {

    private int id;
    private String documentUrl;
    private Date uploadedDate;
    private String uploadedByName;

    public TravelDocumentResponseDTO(
            int id,
            String documentUrl,
            Date uploadedDate,
            String uploadedByName
    ) {
        this.id = id;
        this.documentUrl = documentUrl;
        this.uploadedDate = uploadedDate;
        this.uploadedByName = uploadedByName;
    }

    public int getId() { return id; }
    public String getDocumentUrl() { return documentUrl; }
    public Date getUploadedDate() { return uploadedDate; }
    public String getUploadedByName() { return uploadedByName; }
}
