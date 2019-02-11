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
    private final LetterRepository letterRepository;
    private final JavaMailSender emailSender;

    public LetterServiceImpl(@Autowired LetterRepository letterRepository, JavaMailSender emailSender) {
        this.letterRepository = letterRepository;
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
    public Response<String> onNotificationStatus(UUID uuid) {
        return letterRepository.findById(uuid).map(l -> new Response<>(l.getStatus())).orElseThrow(LetterNotFoundException::new);
    }

    @Override
    public Response<UUID> onSendNotification(Message message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getTo());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getText());

        Letter letter = new Letter(message.getTo(), message.getSubject(), message.getText());
        try {
            emailSender.send(mailMessage);
            letter.setStatus("SUCCESS");
        } catch (MailException e) {
            letter.setStatus(e.getLocalizedMessage());
        }

        letterRepository.save(letter);
        return new Response<>(letter.getUuid());
    }
}
