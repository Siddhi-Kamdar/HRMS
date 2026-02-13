package com.example.backend.entity;

import com.example.backend.entity.DocumentType;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "travel_documents")
public class TravelDocument {
    @Id
    @Column(name = "travel_document_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_url", nullable = false, length = 100)
    private String documentUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    private Employee uploadedBy;

    @Column(name = "uploaded_date", nullable = false)
    private Date uploadedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @Column(name = "is_deleted", nullable = false)
    private String isDeleted;

}