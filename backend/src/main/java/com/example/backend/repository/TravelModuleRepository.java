package com.example.backend.repository;

import com.example.backend.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelModuleRepository extends JpaRepository<Travel, Long> {
    @Query(value = "SELECT travel_id, employees.full_name, destination, depart_date, return_date FROM travel_details\n" +
            "JOIN employees\n" +
            "ON travel_details.employee_id = employees.employee_id", nativeQuery = true )
    List<Travel> findAllTravelDetails();


}
