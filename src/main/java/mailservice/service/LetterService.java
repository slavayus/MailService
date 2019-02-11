package mailservice.service;

import mailservice.entities.Letter;

import java.util.List;
import java.util.Optional;

public interface LetterService {
    void save(Letter letter);

    Optional<Letter> findById(Long id);

    List<Letter> findAll();
}
