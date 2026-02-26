package com.example.backend.repository;

import com.example.backend.entity.CelebrationsLog;
import com.example.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelebrationsLogRepository
        extends JpaRepository<CelebrationsLog, Integer> {

    boolean existsByEmployeeAndCelebrationTypeAndCelebrationYear(
            Employee employee,
            String celebrationType,
            Integer celebrationYear
    );
}