package com.example.backend.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="notifications")
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

    public Integer getNotificationId(){return notificationId;}
    public Employee getEmployee(){return employee;}
    public void setEmployee(Employee employee){this.employee=employee;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}
    public String getMessage(){return message;}
    public void setMessage(String message){this.message=message;}
    public Boolean getIsRead(){return isRead;}
    public void setIsRead(Boolean read){isRead=read;}
}
