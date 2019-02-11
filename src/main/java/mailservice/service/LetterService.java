package mailservice.service;

import mailservice.controllers.model.Message;
import mailservice.controllers.model.Response;
import mailservice.entities.Letter;

import java.util.List;
import java.util.UUID;

public interface LetterService {
    List<Letter> findAll();

    Response<String> onNotificationStatus(UUID uuid);

    Response<UUID> onSendNotification(Message message);
}
