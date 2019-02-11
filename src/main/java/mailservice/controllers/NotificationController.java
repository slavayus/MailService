package mailservice.controllers;

import mailservice.controllers.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final JavaMailSender emailSender;

    @Autowired
    public NotificationController(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/send")
    public String sendNotification(@RequestBody Message message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getTo());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getText());

        emailSender.send(mailMessage);

        return "email sent";
    }

    @PostMapping("/send/{uuid}")
    public String notificationStatus(@PathVariable String uuid) {
        return "email sent";
    }
}
