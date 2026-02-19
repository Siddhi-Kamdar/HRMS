
package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "travel_employees")
public class TravelEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_employee_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Travel getTravel() { return travel; }
    public void setTravel(Travel travel) { this.travel = travel; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}