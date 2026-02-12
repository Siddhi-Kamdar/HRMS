
package com.example.backend.dto.response;

import java.util.Date;

public class TravelResponseDTO {

    private int travelId;
    private String fullName;
    private String destination;
    private Date departDate;
    private Date returnDate;

    public TravelResponseDTO(int travelId, String fullName,
                             String destination, Date departDate,
                             Date returnDate) {
        this.travelId = travelId;
        this.fullName = fullName;
        this.destination = destination;
        this.departDate = departDate;
        this.returnDate = returnDate;
    }

    public int getTravelId() {
        return travelId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDepartDate() {
        return departDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }
}
