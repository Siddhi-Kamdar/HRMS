package com.example.backend.repository;

import com.example.backend.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository
        extends JpaRepository<DocumentType, Integer> {
}