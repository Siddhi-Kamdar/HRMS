package com.example.backend.repository;

import com.example.backend.entity.TravelEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelEmployeeRepository
        extends JpaRepository<TravelEmployee, Integer> {

    List<TravelEmployee> findByTravel_TravelId(int travelId);
}
