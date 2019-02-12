package mailservice.service;

import mailservice.controllers.model.Message;
import mailservice.controllers.model.Response;
import mailservice.entities.Letter;

import java.util.List;
import java.util.UUID;

public interface LetterService {
    List<Letter> findAll();

    Response<String> notificationStatus(UUID uuid);

    Response<UUID> sendNotification(Message message);
}
