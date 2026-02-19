package com.example.backend.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "travel_details")
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private int travelId;

    @Column(name = "destination")
    private String destination;

    @Column(name = "depart_date")
    private Date departDate;

    @Column(name = "return_date")
    private Date returnDate;

    @ManyToOne
    @JoinColumn(name = "scheduler_id")
    private Employee scheduler;

    public int getTravelId() { return travelId; }
    public void setTravelId(int travelId) { this.travelId = travelId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Date getDepartDate() { return departDate; }
    public void setDepartDate(Date departDate) { this.departDate = departDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public Employee getScheduler() { return scheduler; }
    public void setScheduler(Employee scheduler) { this.scheduler = scheduler; }
}