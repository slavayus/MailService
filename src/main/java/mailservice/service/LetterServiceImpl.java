package mailservice.service;

import mailservice.entities.Letter;
import mailservice.repository.LetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
@Service
public class LetterServiceImpl implements LetterService {
    private final LetterRepository letterRepository;

    public LetterServiceImpl(@Autowired LetterRepository letterRepository) {
        this.letterRepository = letterRepository;
    }

    @Override
    public void save(Letter letter) {
        letterRepository.save(letter);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Letter> findById(Long id) {
        return letterRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Letter> findAll() {
        List<Letter> letters = new LinkedList<>();
        letterRepository.findAll().forEach(letters::add);
        return letters;
    }
}
