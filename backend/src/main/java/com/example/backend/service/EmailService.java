package com.example.backend.service;

import com.example.backend.entity.Employee;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

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
        if (author == null || author.getEmail() == null) return;

        sendMail(
                author.getEmail(),
                "Content Removed by HR",
                "Dear " + author.getFullName() + ",\n\n" +
                        "Your post/comment has been removed by HR due to policy violation.\n\n" +
                        "If you believe this was a mistake, please contact HR.\n\n" +
                        "Regards,\nHR Team"
        );
        System.out.println("hiyaa, why you put this kinda post! you idiot!");
    }
    public void sendMailWithAttachment(
            String to,
            String subject,
            String body,
            String filePath) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            FileSystemResource file =
                    new FileSystemResource(new File(filePath));

            helper.addAttachment(file.getFilename(), file);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Email sending failed");
        }
    }
}