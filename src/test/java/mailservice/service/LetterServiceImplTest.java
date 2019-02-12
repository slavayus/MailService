package mailservice.service;

import mailservice.controllers.model.Message;
import mailservice.controllers.model.Response;
import mailservice.entities.Letter;
import mailservice.repository.LetterRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class LetterServiceImplTest {

    @MockBean
    public LetterRepository letterRepository;

    @MockBean
    public JavaMailSender javaMailSender;

    @TestConfiguration
    static class LetterServiceImplTestContextConfiguration {
        @Bean
        public LetterService provideLetterService() {
            return new LetterServiceImpl();
        }
    }

    @Autowired
    private LetterService letterService;

    @Test
    public void findAll() {
        LinkedList<Letter> letters = new LinkedList<>();
        Letter letter = Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").status("SUCCESS").build();
        letters.add(letter);
        Mockito.when(letterRepository.findAll()).thenReturn(letters);

        assertEquals(letterService.findAll().size(), 1);
        assertEquals(letterService.findAll().get(0).getTo(), "AlexTo");
    }

    @Test
    public void onNotificationStatus() {
        Letter letter = Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").status("FAIL").build();
        UUID uuid = UUID.randomUUID();
        Mockito.when(letterRepository.findById(uuid)).thenReturn(Optional.of(letter));

        assertEquals(letterService.onNotificationStatus(uuid), new Response<>("FAIL"));
    }

    @Test
    public void onSendNotification() {
        Message message = new Message();
        message.setTo("slavayus@gmail.com");
        message.setSubject("YEEE");
        message.setText("EEEE");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getTo());
        mailMessage.setText(message.getText());
        mailMessage.setSubject(message.getSubject());

        Mockito.doNothing().when(javaMailSender).send(mailMessage);

        Letter letter = Letter.builder().to(message.getTo()).subject(message.getSubject()).text(message.getText()).status("SUCCESS").build();

        assertEquals(letterService.onSendNotification(message), new Response<>(letter.getUuid()));
    }
}