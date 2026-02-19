package com.example.backend.repository;

import com.example.backend.entity.TravelDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelDocumentRepository
        extends JpaRepository<TravelDocument, Integer> {

    List<TravelDocument> findByTravel_TravelId(int travelId);

    List<TravelDocument> findByTravel_TravelIdAndEmployee_EmployeeId(
            int travelId, int employeeId
    );

    List<TravelDocument> findByTravel_TravelIdAndEmployeeIsNull(int travelId);

}