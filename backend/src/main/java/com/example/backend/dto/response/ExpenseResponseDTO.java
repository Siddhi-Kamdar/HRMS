package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ExpenseResponseDTO {

    private Integer expenseId;
    private String employeeName;
    private String destination;
    private BigDecimal amount;
    private String status;
    private String remark;
    private Date expenseDate;
    private String proofUrl;
    private Integer travelId;

}
