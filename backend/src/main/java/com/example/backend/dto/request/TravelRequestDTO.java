
package com.example.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TravelRequestDTO {

    private List<Long> employeeIds;
    private Long schedulerId;
    private String destination;
    private Date departDate;
    private Date returnDate;

}
