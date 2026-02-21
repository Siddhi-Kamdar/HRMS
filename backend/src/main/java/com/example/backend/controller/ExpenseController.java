
package com.example.backend.controller;

import com.example.backend.dto.response.ExpenseResponseDTO;
import com.example.backend.entity.ExpensesDetail;
import com.example.backend.service.ExpenseProofService;
import com.example.backend.service.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseProofService expenseProofService;

    @PostMapping("/upload-proof")
    @PreAuthorize("hasAnyRole('EMPLOYEE','HR','MANAGER')")
    public ResponseEntity<String> uploadProof(
            @RequestParam("file") MultipartFile file) {

        String proofUrl =
                expenseProofService.uploadProof(file);

        return ResponseEntity.ok(proofUrl);
    }

    @PostMapping(
            value = "",
            consumes = "multipart/form-data"
    )
    @PreAuthorize("hasAnyRole('EMPLOYEE','HR','MANAGER')")
    public ResponseEntity<String> createExpense(

            @RequestParam Integer travelId,
            @RequestParam Integer expenseTypeId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String comment,
            @RequestParam("file") MultipartFile file
    ) {

        String proofUrl =
                expenseProofService.uploadProof(file);

        expenseService.createExpense(
                        travelId,
                        expenseTypeId,
                        amount,
                        comment,
                        proofUrl
                );
//        return ResponseEntity.ok(
//                expenseService.createExpense(
//                        travelId,
//                        expenseTypeId,
//                        amount,
//                        comment,
//                        proofUrl
//                ));
        return ResponseEntity.ok("expense added successfully");
    }



    @GetMapping("/my")
    @PreAuthorize(
            "hasAnyRole('EMPLOYEE','HR','MANAGER')")
    public ResponseEntity<List<ExpenseResponseDTO>>
    myExpenses(){

        return ResponseEntity.ok(
                expenseService.getMyExpenses());
    }



    @GetMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<ExpenseResponseDTO>>
    allExpenses(){

        return ResponseEntity.ok(
                expenseService.getAllExpenses());
    }

    @GetMapping("/team")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<ExpenseResponseDTO>>
    teamExpenses(){

        return ResponseEntity.ok(
                expenseService.getTeamExpenses());
    }
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('HR')")
    public void approve(
            @PathVariable Integer id,
            @RequestParam Integer hrId){

        expenseService.approveExpense(id,hrId);
    }



    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('HR')")
    public void reject(
            @PathVariable Integer id,
            @RequestParam Integer hrId,
            @RequestParam String remark){

        expenseService.rejectExpense(
                id,hrId,remark);
    }

    @GetMapping("/travel-total/{travelId}")
    @PreAuthorize(
            "hasAnyRole('EMPLOYEE','HR','MANAGER')")
    public Double total(
            @PathVariable Integer travelId){

        return expenseService
                .getTotalByTravel(travelId);
    }
}