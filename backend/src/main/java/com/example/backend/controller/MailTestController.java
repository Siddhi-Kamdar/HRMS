package com.example.backend.controller;

import com.example.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test-mail")
    public String testMail(){

        emailService.sendMail(
                "siddhikamdar1624@gmail.com",
                "Test Mail",
                "Mail configuration working"
        );

        return "Mail Sent";
    }
}