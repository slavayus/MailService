package mailservice.controllers;

import mailservice.controllers.model.Message;
import mailservice.controllers.model.Response;
import mailservice.entities.Letter;
import mailservice.service.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
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
    public Response<Long> sendNotification(@RequestBody Message message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getTo());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getText());

        Letter letter = new Letter(message.getTo(), message.getSubject(), message.getText());
        try {
            emailSender.send(mailMessage);
            letter.setStatus("SUCCESS");
        } catch (MailException e) {
            letter.setStatus(e.getLocalizedMessage());
        }
        letterService.save(letter);

        return new Response<>(letter.getId());
    }

    @GetMapping("/send/{id}")
    public Response<String> notificationStatus(@PathVariable long id) {
        return new Response<>(letterService.findById(id).map(Letter::getStatus).orElse("There is no such letter"));
    }

    @GetMapping
    public List<Letter> lettersList() {
        return letterService.findAll();
    }
}
