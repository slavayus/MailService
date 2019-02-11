package mailservice.service;

import mailservice.entities.Letter;

import java.util.List;
import java.util.UUID;

public interface LetterService {
    void save(Letter letter);

    Letter findByUUID(UUID uuid);

    List<Letter> findAll();
}
