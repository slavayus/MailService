package mailservice.controllers;

import mailservice.controllers.model.Message;
import mailservice.controllers.model.Response;
import mailservice.entities.Letter;
import mailservice.service.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final LetterService letterService;

    @Autowired
    public NotificationController(LetterService letterService) {
        this.letterService = letterService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@Valid @RequestBody Message message, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            LinkedList<String> errors = new LinkedList<>();
            bindingResult.getAllErrors().forEach(e -> errors.add(e.getDefaultMessage()));
            return badRequest().body(new Response<>(errors));
        }
        return ok(letterService.onSendNotification(message));
    }

    @GetMapping("/send/{uuid}")
    public ResponseEntity<?> notificationStatus(@PathVariable UUID uuid) {
        return ok(letterService.onNotificationStatus(uuid));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "PathVariable must be UUID")
    public void handleIllegalArgumentException() {
    }

    @GetMapping
    public List<Letter> lettersList() {
        return letterService.findAll();
    }
}
