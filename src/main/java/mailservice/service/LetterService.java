package mailservice.service;

import mailservice.entities.Letter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LetterService {
    void save(Letter letter);

    Optional<Letter> findByUUID(UUID uuid);

    List<Letter> findAll();
}
