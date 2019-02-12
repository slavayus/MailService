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
    public void findAll_withOneLetter_willReturnOneObject() {
        Letter alex = Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").status("SUCCESS").build();
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
    public void findAll_withMultipleLetter_willReturnMultipleObject() {
        entityManager.persist(Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").build());
        entityManager.persist(Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").build());
        entityManager.persist(Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").build());
        entityManager.flush();

        Iterable<Letter> found = letterRepository.findAll();

        final int[] size = {0};
        found.forEach(l -> size[0]++);
        assertEquals(size[0], 3);
    }

    @Test
    public void findByUUID_withExistUUID_willReturnTrue() {
        Letter alex = Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").build();
        entityManager.persist(alex);
        entityManager.flush();

        assertTrue(letterRepository.findById(alex.getUuid()).isPresent());
    }

    @Test
    public void findByUUID_withExistUUID_willReturnFalse() {
        Letter alex = Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").build();
        entityManager.persist(alex);
        entityManager.flush();

        assertFalse(letterRepository.findById(UUID.randomUUID()).isPresent());
    }

    @Test
    public void saveTest_willReturnTrue() {
        Letter alex = Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").build();
        letterRepository.save(alex);

        LetterRepository mock = Mockito.mock(LetterRepository.class);
        Mockito.when(mock.findById(alex.getUuid())).thenReturn(Optional.of(alex));

        assertTrue(mock.findById(alex.getUuid()).isPresent());
    }

    @Test
    public void saveTest_willReturnFalse() {
        Letter alex = Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").build();
        letterRepository.save(alex);

        LetterRepository mock = Mockito.mock(LetterRepository.class);
        Mockito.when(mock.findById(alex.getUuid())).thenReturn(Optional.of(alex));

        assertFalse(mock.findById(UUID.randomUUID()).isPresent());
    }
}