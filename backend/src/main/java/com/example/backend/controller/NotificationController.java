
package com.example.backend.controller;

import com.example.backend.entity.Notification;
import com.example.backend.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @GetMapping("/my")
    public List<Notification> myNotifications(){
        return notificationService.getMyNotifications();
    }


    @GetMapping("/unread-count")
    public long unreadCount(){
        return notificationService.getUnreadCount();
    }


    @PutMapping("/read/{id}")
    public void markRead(
            @PathVariable Integer id){
        notificationService.markAsRead(id);
    }


    @PutMapping("/read-all")
    public void markAll(){
        notificationService.markAllRead();
    }
}