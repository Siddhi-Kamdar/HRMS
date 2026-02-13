package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "document_types")
public class DocumentType{

    @Id
    @Column(name = "document_type_id")
    private int id;

    @Column(name = "document_type__name", nullable = false, length = 50)
    private String documentType_Name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentType_Name() {
        return documentType_Name;
    }

    public void setDocumentType_Name(String documentType_Name) {
        this.documentType_Name = documentType_Name;
    }
}