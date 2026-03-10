package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TravelResponseDTO {

    private int travelId;
    private List<String> employeeNames;
    private List<Integer> employeeIds;
    private String destination;
    private Date departDate;
    private Date returnDate;
}

