package mailservice.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "Operations pertaining to notification")
public class NotificationController {
    private final LetterService letterService;

    @Autowired
    public NotificationController(LetterService letterService) {
        this.letterService = letterService;
    }

    @PostMapping("/send")
    @ApiOperation(value = "Send a new notification", response = Response.class)
    public ResponseEntity<?> sendNotification(@Valid @RequestBody Message message, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new LinkedList<>();
            bindingResult.getAllErrors().forEach(e -> errors.add(e.getDefaultMessage()));
            return badRequest().body(new Response<>(errors));
        }
        return ok(letterService.onSendNotification(message));
    }

    @GetMapping("/send/{uuid}")
    @ApiOperation(value = "Search a notification status with an UUID", response = Response.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<?> notificationStatus(@PathVariable UUID uuid) {
        return ok(letterService.onNotificationStatus(uuid));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "PathVariable must be UUID")
    public void handleIllegalArgumentException() {
    }

    @GetMapping
    @ApiOperation(value = "View a list of notifications")
    public List<Letter> lettersList() {
        return letterService.findAll();
    }
}
