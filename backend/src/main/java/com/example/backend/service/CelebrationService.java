package com.example.backend.service;

import com.example.backend.entity.*;
import com.example.backend.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CelebrationService {

    private final EmployeeRepository employeeRepository;
    private final PostRepository postRepository;
    private final CelebrationsLogRepository celebrationsLogRepository;

    @PostConstruct
    public void runOnStartup() {
        generateDailyCelebrations();
    }

    //cron = "0 0 0 * * ?"
    @Scheduled(cron = "0 0 0 * * ?") //fixedRate = 60000
    @Transactional
    public void generateDailyCelebrations() {

        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            if ("system@hrms.com".equalsIgnoreCase(employee.getEmail())) {
                continue;
            }
            handleBirthday(employee, today, currentYear);
            handleWorkAnniversary(employee, today, currentYear);
        }
    }

    private void handleBirthday(Employee employee, LocalDate today, int year) {

        if (employee.getDateOfBirth() == null) return;

        LocalDate dob = employee.getDateOfBirth()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (dob.getMonth() == today.getMonth()
                && dob.getDayOfMonth() == today.getDayOfMonth()) {

            boolean exists =
                    celebrationsLogRepository.existsByEmployeeAndCelebrationTypeAndCelebrationYear(
                            employee, "BIRTHDAY", year
                    );

            if (!exists) {
                createBirthdayPost(employee);
                logCelebration(employee, "BIRTHDAY", year);
            }
        }
    }

    private void handleWorkAnniversary(Employee employee, LocalDate today, int year) {

        if (employee.getJoiningDate() == null) return;

        LocalDate joiningDate = employee.getJoiningDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (joiningDate.getMonth() == today.getMonth()
                && joiningDate.getDayOfMonth() == today.getDayOfMonth()) {

            int yearsCompleted = year - joiningDate.getYear();

            if (yearsCompleted > 0) {

                boolean exists =
                        celebrationsLogRepository.existsByEmployeeAndCelebrationTypeAndCelebrationYear(
                                employee, "ANNIVERSARY", year
                        );

                if (!exists) {
                    createAnniversaryPost(employee, yearsCompleted);
                    logCelebration(employee, "ANNIVERSARY", year);
                }
            }
        }
    }

    private void createBirthdayPost(Employee employee) {

        Post post = new Post();
        post.setAuthor(getSystemUser());
        post.setTitle("Happy Birthday 🎉");
        post.setDescription("Join us in wishing "
                + employee.getFullName()
                + " a wonderful birthday!");
        post.setSystemGenerated(true);
        post.setDeleted(false);
        post.setCreatedDate(LocalDateTime.now());

        postRepository.save(post);
    }

    private void createAnniversaryPost(Employee employee, int years) {

        Post post = new Post();
        post.setAuthor(getSystemUser());
        post.setTitle("Work Anniversary 🎊");
        post.setDescription(employee.getFullName()
                + " completes "
                + years
                + " year(s) with the organization. Congratulations!");
        post.setSystemGenerated(true);
        post.setDeleted(false);
        post.setCreatedDate(LocalDateTime.now());

        postRepository.save(post);
    }

    private Employee getSystemUser() {
        return employeeRepository
                .findByEmail("system@hrms.com")
                .orElseThrow(() -> new RuntimeException("System user not found"));
    }
    private void logCelebration(Employee employee, String type, int year) {

        CelebrationsLog log = new CelebrationsLog();
        log.setEmployee(employee);
        log.setCelebrationType(type);
        log.setCelebrationYear(year);
        log.setCreatedAt(LocalDateTime.now());

        celebrationsLogRepository.save(log);
    }
}