package com.example.backend.service;

import com.example.backend.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(
            String to,
            String subject,
            String body){

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendWarning(Employee author) {
//        SimpleMailMessage message =
//                new SimpleMailMessage();
//
//        message.setTo(author.getEmail());
//        message.setSubject("Post Deleted due to Inappropriate Content");
//        message.setText("Hi, \n This is to Inform you your post is deleted by HR, due to xyz reasons");
        System.out.println("hiyaa, why you put this kinda post! you idiot!");
    }
}