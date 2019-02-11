package mailservice.controllers;

import mailservice.controllers.model.Message;
import mailservice.entities.Letter;
import mailservice.service.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final LetterService letterService;
    private final JavaMailSender emailSender;

    @Autowired
    public NotificationController(LetterService letterService, JavaMailSender emailSender) {
        this.letterService = letterService;
        this.emailSender = emailSender;
    }

    @PostMapping("/send")
    public String sendNotification(@RequestBody Message message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getTo());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getText());

        emailSender.send(mailMessage);

        Letter letter = new Letter(message.getTo(), message.getSubject(), message.getText(), "SUCCESS");
        letterService.save(letter);

        return "Letter uuid " + letter.getId();
    }

    @PostMapping("/send/{uuid}")
    public String notificationStatus(@PathVariable String uuid) {
        return "email sent";
    }
}
