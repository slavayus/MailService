package mailservice.controllers;

import mailservice.controllers.model.Message;
import mailservice.controllers.model.Response;
import mailservice.entities.Letter;
import mailservice.service.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

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
    public ResponseEntity<?> sendNotification(@Valid @RequestBody Message message, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            LinkedList<String> errors = new LinkedList<>();
            bindingResult.getAllErrors().forEach(e -> errors.add(e.getDefaultMessage()));
            return badRequest().body(new Response<>(errors));
        }
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

        return ok(new Response<>(letter.getId()));
    }

    @GetMapping("/send/{id}")
    public ResponseEntity<?> notificationStatus(@PathVariable long id) {
        return letterService.findById(id).map(l -> ok(new Response<>(l.getStatus()))).orElse(badRequest().body(new Response<>("There is no such letter")));
    }

    @GetMapping
    public List<Letter> lettersList() {
        return letterService.findAll();
    }
}
