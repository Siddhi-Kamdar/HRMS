
package com.example.backend.dto.request;

import java.util.Date;
import java.util.List;

public class TravelRequestDTO {

    private List<Long> employeeIds;
    private Long schedulerId;
    private String destination;
    private Date departDate;
    private Date returnDate;

    public List<Long> getEmployeeIds() { return employeeIds; }
    public void setEmployeeIds(List<Long> employeeIds) { this.employeeIds = employeeIds; }

    public Long getSchedulerId() { return schedulerId; }
    public void setSchedulerId(Long schedulerId) { this.schedulerId = schedulerId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Date getDepartDate() { return departDate; }
    public void setDepartDate(Date departDate) { this.departDate = departDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
}
