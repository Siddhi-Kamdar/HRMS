package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private int job_id;

    @Column(name = "job_title")
    private String job_title;

    @Column(name = "job_summary")
    private String job_summary;

    @Column(name = "job_description_url")
    private String job_description_url;

    @Column(name = "job_status")
    private String job_status;

    @ManyToOne
    @JoinColumn(name = "posted_by_id")
    private Employee posted_by_employee;

    @Column(name = "posted_date")
    private Date posted_date;

}
