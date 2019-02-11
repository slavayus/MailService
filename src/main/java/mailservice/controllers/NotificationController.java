package mailservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    private final JavaMailSender emailSender;

    @Autowired
    public NotificationController(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/notification/send")
    public String sendNotification() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("slavayus@gmail.com");
        message.setSubject("YEEE");
        message.setText("YYYY");

        emailSender.send(message);

        return "email sent";
    }

    @PostMapping("/notification/send/{uuid}")
    public String notificationStatus(@PathVariable String uuid) {
        return "email sent";
    }
}
