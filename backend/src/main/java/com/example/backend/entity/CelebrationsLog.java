package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "celebrations_log")
@Getter
@Setter
public class CelebrationsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "celebration_type", nullable = false)
    private String celebrationType;

    @Column(name = "celebration_year", nullable = false)
    private Integer celebrationYear;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}