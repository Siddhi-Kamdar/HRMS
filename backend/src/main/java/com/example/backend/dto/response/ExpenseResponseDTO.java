package com.example.backend.dto.response;

import java.math.BigDecimal;
import java.util.Date;

public class ExpenseResponseDTO {

    private Integer expenseId;
    private String employeeName;
    private String destination;
    private BigDecimal amount;
    private String status;
    private String remark;
    private Date expenseDate;
    private String proofUrl;

    public ExpenseResponseDTO(
            Integer expenseId,
            String employeeName,
            String destination,
            BigDecimal amount,
            String status,
            String remark,
            Date expenseDate,
            String proofUrl) {

        this.expenseId = expenseId;
        this.employeeName = employeeName;
        this.destination = destination;
        this.amount = amount;
        this.status = status;
        this.remark = remark;
        this.expenseDate = expenseDate;
        this.proofUrl = proofUrl;
    }

    public Integer getExpenseId() { return expenseId; }
    public String getEmployeeName() { return employeeName; }
    public String getDestination() { return destination; }
    public BigDecimal getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getRemark() { return remark; }
    public Date getExpenseDate() { return expenseDate; }
    public String getProofUrl() { return proofUrl; }
}
