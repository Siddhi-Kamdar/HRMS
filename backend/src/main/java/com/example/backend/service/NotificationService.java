package com.example.backend.service;

import com.example.backend.entity.Employee;
import com.example.backend.entity.Notification;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LoggedInUserService loggedInUserService;


    public void create(
            Employee employee,
            String title,
            String message){

        Notification n = new Notification();
        n.setEmployee(employee);
        n.setTitle(title);
        n.setMessage(message);

        notificationRepository.save(n);
    }


    public List<Notification> getMyNotifications(){

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        Employee employee =
                employeeRepository
                        .findByEmail(email)
                        .orElseThrow();

        return notificationRepository
                .findByEmployeeEmployeeIdOrderByCreatedAtDesc(
                        employee.getEmployeeId());
    }


    public long getUnreadCount(){

        Employee emp =
                loggedInUserService
                        .getLoggedInEmployee();

        return notificationRepository
                .countByEmployeeEmployeeIdAndIsReadFalse(
                        emp.getEmployeeId());
    }

    public void markAsRead(Integer notificationId){

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow();

        notification.setIsRead(true);

        notificationRepository.save(notification);
    }

    public void markAllRead(){

        Employee emp =
                loggedInUserService
                        .getLoggedInEmployee();

        List<Notification> list =
                notificationRepository
                        .findByEmployeeEmployeeIdOrderByCreatedAtDesc(
                                emp.getEmployeeId());

        list.forEach(n -> n.setIsRead(true));

        notificationRepository.saveAll(list);
    }

}