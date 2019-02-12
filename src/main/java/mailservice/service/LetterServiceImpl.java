package mailservice.service;

import mailservice.controllers.exceptions.LetterNotFoundException;
import mailservice.controllers.model.Message;
import mailservice.controllers.model.Response;
import mailservice.entities.Letter;
import mailservice.repository.LetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
@Service
public class LetterServiceImpl implements LetterService {
    private LetterRepository letterRepository;
    private JavaMailSender emailSender;

    @Autowired
    public void setLetterRepository(LetterRepository letterRepository) {
        this.letterRepository = letterRepository;
    }

    @Autowired
    public void setEmailSender(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Letter> findAll() {
        List<Letter> letters = new LinkedList<>();
        letterRepository.findAll().forEach(letters::add);
        return letters;
    }

    @Override
    @Transactional(readOnly = true)
    public Response<String> notificationStatus(UUID uuid) {
        return letterRepository.findById(uuid).map(l -> new Response<>(l.getStatus())).orElseThrow(LetterNotFoundException::new);
    }

    @Override
    public Response<UUID> sendNotification(Message message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getTo());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getText());

        Letter.LetterBuilder letterBuilder = Letter.builder().to(message.getTo()).subject(message.getSubject()).text(message.getText());
        try {
            emailSender.send(mailMessage);
            letterBuilder.status("SUCCESS");
        } catch (MailException e) {
            letterBuilder.status(e.getLocalizedMessage());
        }

        Letter letter = letterBuilder.build();
        letterRepository.save(letter);
        return new Response<>(letter.getUuid());
    }
}
