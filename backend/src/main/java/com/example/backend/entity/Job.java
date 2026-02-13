package com.example.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "jobs")
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

    public Employee getPosted_by_employee() {
        return posted_by_employee;
    }

    public void setPosted_by_employee(Employee posted_by_employee) {
        this.posted_by_employee = posted_by_employee;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_summary() {
        return job_summary;
    }

    public void setJob_summary(String job_summary) {
        this.job_summary = job_summary;
    }

    public String getJob_description_url() {
        return job_description_url;
    }

    public void setJob_description_url(String job_description_url) {
        this.job_description_url = job_description_url;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public Date getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(Date posted_date) {
        this.posted_date = posted_date;
    }
}
