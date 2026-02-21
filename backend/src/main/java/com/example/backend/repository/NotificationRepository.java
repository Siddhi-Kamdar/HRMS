package com.example.backend.repository;

import com.example.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification,Integer>{

    List<Notification>
    findByEmployeeEmployeeIdOrderByCreatedAtDesc(
            Integer employeeId);

    long countByEmployeeEmployeeIdAndIsReadFalse(
            Integer employeeId);
}