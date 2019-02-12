package mailservice.repository;

import mailservice.entities.Letter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LetterRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LetterRepository letterRepository;

    @Test
    public void findAllTestOne() {
        Letter alex = new Letter("AlexTo", "AlexSubject", "AlexText");
        alex.setStatus("SUCCESS");

        entityManager.persist(alex);
        entityManager.flush();

        Iterable<Letter> found = letterRepository.findAll();

        final int[] size = {0};
        found.forEach(l -> size[0]++);
        assertEquals(size[0], 1);
        Letter next = found.iterator().next();
        assertEquals(next.getTo(), "AlexTo");
        assertEquals(next.getSubject(), "AlexSubject");
        assertEquals(next.getText(), "AlexText");
        assertEquals(next.getStatus(), "SUCCESS");
    }

    @Test
    public void findAllTestMultiple() {
        entityManager.persist(new Letter("AlexTo", "AlexSubject", "AlexText"));
        entityManager.persist(new Letter("AlexTo", "AlexSubject", "AlexText"));
        entityManager.persist(new Letter("AlexTo", "AlexSubject", "AlexText"));
        entityManager.flush();

        Iterable<Letter> found = letterRepository.findAll();

        final int[] size = {0};
        found.forEach(l -> size[0]++);
        assertEquals(size[0], 3);
    }

    @Test
    public void findByUUIDExists() {
        Letter alex = new Letter("AlexTo", "AlexSubject", "AlexText");
        entityManager.persist(alex);
        entityManager.flush();

        assertTrue(letterRepository.findById(alex.getUuid()).isPresent());
    }

    @Test
    public void findByUUIDNonExists() {
        Letter alex = new Letter("AlexTo", "AlexSubject", "AlexText");
        entityManager.persist(alex);
        entityManager.flush();

        assertFalse(letterRepository.findById(UUID.fromString(alex.getUuid().toString().substring(0, alex.getUuid().toString().length() - 1))).isPresent());
    }

    @Test
    public void saveTestExists() {
        Letter alex = new Letter("AlexTo", "AlexSubject", "AlexText");
        letterRepository.save(alex);

        LetterRepository mock = Mockito.mock(LetterRepository.class);
        Mockito.when(mock.findById(alex.getUuid())).thenReturn(Optional.of(alex));

        assertTrue(mock.findById(alex.getUuid()).isPresent());
    }

    @Test
    public void saveTestNo() {
        Letter alex = new Letter("AlexTo", "AlexSubject", "AlexText");
        letterRepository.save(alex);

        LetterRepository mock = Mockito.mock(LetterRepository.class);
        Mockito.when(mock.findById(alex.getUuid())).thenReturn(Optional.of(alex));

        assertFalse(mock.findById(UUID.fromString(alex.getUuid().toString().substring(0, alex.getUuid().toString().length() - 1))).isPresent());
    }
}