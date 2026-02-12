package com.example.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "travel_details")
public class Travel {
    @Id
    @Column(name = "travel_id")
    private int travel_id;

    @Column(name = "full_name")
    private String full_name;

//    @OneToOne
//    @JoinColumn(name = "employee_id" )
//    private Employee employee;

    @Column(name = "destination")
    private String destination;

    @Column(name = "depart_date")
    private Date depart_date;

    @Column(name = "return_date")
    private Date return_date;

//    public Employee getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(Employee employee) {
//        this.employee = employee;
//    }

    public void setId(int id) {
        this.travel_id = id;
    }

    public int getId() {
        return travel_id;
    }

    public int getTravel_id() {
        return travel_id;
    }

    public void setTravel_id(int travel_id) {
        this.travel_id = travel_id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepart_date() {
        return depart_date;
    }

    public void setDepart_date(Date depart_date) {
        this.depart_date = depart_date;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }
}
