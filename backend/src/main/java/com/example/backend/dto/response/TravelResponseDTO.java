package com.example.backend.dto.response;

import java.util.Date;
import java.util.List;

public class TravelResponseDTO {

    private int travelId;
    private List<String> employeeNames;
    private String destination;
    private Date departDate;
    private Date returnDate;

    public TravelResponseDTO(int travelId, List<String> employeeNames,
                             String destination, Date departDate,
                             Date returnDate) {
        this.travelId = travelId;
        this.employeeNames = employeeNames;
        this.destination = destination;
        this.departDate = departDate;
        this.returnDate = returnDate;
    }

    public int getTravelId() { return travelId; }
    public List<String> getEmployeeNames() { return employeeNames; }
    public String getDestination() { return destination; }
    public Date getDepartDate() { return departDate; }
    public Date getReturnDate() { return returnDate; }
}

