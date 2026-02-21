package com.example.backend.service;

import com.example.backend.dto.response.ExpenseResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpensesRepository expenseRepo;

    @Autowired
    private TravelModuleRepository travelRepo;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ExpensesReviewRepository reviewRepo;

    @Autowired
    private ApprovalStatusRepository statusRepo;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoggedInUserService loggedInUserService;

    @Autowired
    private TravelEmployeeRepository travelEmployeeRepository;


    public ExpensesDetail createExpense(
            Integer travelId,
            Integer expenseTypeId,
            BigDecimal amount,
            String comment,
            String proofUrl){

        if(proofUrl == null || proofUrl.isEmpty())
            throw new RuntimeException("Proof required");

        Travel travel =
                travelRepo.findById(travelId.longValue())
                        .orElseThrow();



        Date today = new Date();

        if(today.before(travel.getDepartDate()))
            throw new RuntimeException(
                    "Expense not allowed before travel start");

        long diff =
                today.getTime()
                        - travel.getReturnDate().getTime();

        long days = diff/(1000*60*60*24);

        if(days > 10)
            throw new RuntimeException(
                    "Expense submission window closed");

        Employee employee = getLoggedInEmployee();

        boolean assigned = travelEmployeeRepository.findByTravel_TravelId(travelId)
                .stream().anyMatch(te -> te.getEmployee().
                        getEmployeeId() == employee.getEmployeeId());

        if(!assigned){
            throw new RuntimeException("you are not assinged to this travel");
        }

        ExpensesDetail expense = new ExpensesDetail();
        expense.setTravel(travel);
        expense.setEmployee(employee);
        expense.setAmount(amount);
        expense.setComment(comment);
        expense.setProofUrl(proofUrl);
        expense.setUploadDate(today);
        expense.setExpenseDate(today);

        ExpensesDetail savedExpense =
                expenseRepo.save(expense);

//        employeeRepository.findAll()
//                .stream()
//                .filter(e ->
//                        e.getRole()
//                                .getRoleName()
//                                .equalsIgnoreCase("HR"))
//                .forEach(hrEmployee -> {
//
//                    notificationService.create(
//                            hrEmployee,
//                            "New Expense Submitted",
//                            employee.getFullName()
//                                    + " submitted expense for "
//                                    + travel.getDestination()
//                    );
//    try{
//        emailService.sendMail(
//                hrEmployee.getEmail(),
//                "New Expense Submitted",
//                employee.getFullName()
//                        + " submitted expense for "
//                        + travel.getDestination()
//        );
//    }catch (Exception e){
//        System.out.println("mail sending failed!! ");
//    }
//
//                });

        return savedExpense;
    }



    public void approveExpense(
            Integer expenseId,
            Integer hrId){


        saveReview(expenseId,hrId,3,null);
    }



    public void rejectExpense(
            Integer expenseId,
            Integer hrId,
            String remark){

        if(remark==null || remark.isEmpty())
            throw new RuntimeException("Remark required");

        saveReview(expenseId,hrId,4,remark);
    }



    private void saveReview(
            Integer expenseId,
            Integer hrId,
            Integer statusId,
            String comment){

        ExpensesDetail expense =
                expenseRepo.findById(expenseId)
                        .orElseThrow();

        Employee reviewer =
                employeeRepository
                        .findById(hrId.longValue())
                        .orElseThrow();

        ApprovalStatus status =
                statusRepo.findById(statusId)
                        .orElseThrow();

        ExpensesReview review =
                new ExpensesReview();

        review.setExpense(expense);
        review.setReviewedBy(reviewer);
        review.setStatus(status);
        review.setComment(comment);

        reviewRepo.save(review);

        Employee expenseOwner =
                expense.getEmployee();

//        notificationService.create(
//                expenseOwner,
//                "Expense Status Updated",
//                "Your expense is "
//                        + status.getApprovalStatusName()
//        );
//    try{
//        emailService.sendMail(
//                expenseOwner.getEmail(),
//                "Expense Status Updated",
//                "Your expense is "
//                        + status.getApprovalStatusName()
//                        + (comment != null
//                        ? "\nRemark: " + comment
//                        : "")
//        );
//    }catch (Exception e){
//        System.out.println("mail sending failed!! ");
//    }
    }



    private Employee getLoggedInEmployee(){

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return employeeRepository
                .findByEmail(email)
                .orElseThrow();
    }



    public List<ExpenseResponseDTO> getMyExpenses(){

        Employee emp = loggedInUserService.getLoggedInEmployee();

        return expenseRepo
                .findByEmployeeEmployeeId(
                        emp.getEmployeeId())
                .stream()
                .map(this::mapExpense)
                .toList();
    }

    private ExpenseResponseDTO mapExpense(ExpensesDetail expense){

        ExpensesReview review =
                reviewRepo
                        .findTopByExpenseExpenseIdOrderByTimestampDesc(
                                expense.getExpenseId());

        String status =
                review!=null
                        ? review.getStatus()
                        .getApprovalStatusName()
                        : "SUBMITTED";

        String remark =
                review!=null
                        ? review.getComment()
                        : null;

        return new ExpenseResponseDTO(
                expense.getExpenseId(),
                expense.getEmployee().getFullName(),
                expense.getTravel().getDestination(),
                expense.getAmount(),
                status,
                remark,
                expense.getExpenseDate(),
                expense.getProofUrl()
        );
    }

    public List<ExpenseResponseDTO> getAllExpenses(){

        return expenseRepo.findAll()
                .stream()
                .map(this::mapExpense)
                .toList();
    }

    public List<ExpenseResponseDTO> getTeamExpenses(){

        Employee manager = getLoggedInEmployee();

        List<Employee> team =
                employeeRepository
                        .findBySupervisor(manager);

        List<Integer> ids =
                team.stream()
                        .map(Employee::getEmployeeId)
                        .toList();

        return expenseRepo.findAll()
                .stream()
                .filter(e ->
                        ids.contains(
                                e.getEmployee()
                                        .getEmployeeId()))
                .map(this::mapExpense)
                .toList();
    }
    public Double getTotalByTravel(Integer travelId){

        return expenseRepo
                .findByTravelTravelId(travelId)
                .stream()
                .map(e -> e.getAmount().doubleValue())
                .reduce(0.0, Double::sum);
    }
}