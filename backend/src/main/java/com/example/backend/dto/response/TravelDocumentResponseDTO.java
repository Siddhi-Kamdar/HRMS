package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TravelDocumentResponseDTO {
    private int id;
    private String documentUrl;
    private Date uploadedDate;
    private String uploadedByName;
    private int employeeId;
    private int uploadedById;
}