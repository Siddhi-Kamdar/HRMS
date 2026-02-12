package com.example.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @Column(name = "employee_id")
    private int id;

    private String full_name;
    private Date joining_date;
    private Date date_of_birth;
    private String email;
    private String password;
    private String contact_number;
    private String emergency_contact;
    private String address;
    private int supervisior_id;
    private  int department_id;
    private int role_id;
}
