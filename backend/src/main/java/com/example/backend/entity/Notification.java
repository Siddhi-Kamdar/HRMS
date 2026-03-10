package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer notificationId;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    private String title;
    private String message;

    @Column(name="is_read")
    private Boolean isRead=false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt=new Date();

}
