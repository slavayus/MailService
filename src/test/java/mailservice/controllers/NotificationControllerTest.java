package mailservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mailservice.controllers.exceptions.LetterNotFoundException;
import mailservice.controllers.model.Message;
import mailservice.controllers.model.Response;
import mailservice.entities.Letter;
import mailservice.service.LetterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NotificationController.class)
@TestPropertySource(locations = "classpath:/messages/hibernate-validation.properties")
public class NotificationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LetterService service;

    @Value("${validation.to.message}")
    private String toMessage;
    @Value("${validation.subject.message}")
    private String subjectMessage;
    @Value("${validation.text.message}")
    private String textMessage;

    @Test
    public void notificationStatus_willReturnSuccess() throws Exception {
        UUID uuid = UUID.randomUUID();
        given(service.onNotificationStatus(uuid)).willReturn(new Response<>("SUCCESS"));
        mvc.perform(get("/notification/send/" + uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is("SUCCESS")));
    }

    @Test
    public void notificationStatus_willReturnError() throws Exception {
        UUID uuid = UUID.randomUUID();
        given(service.onNotificationStatus(uuid)).willThrow(LetterNotFoundException.class);
        mvc.perform(get("/notification/send/" + uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("There is no such letter"));
    }

    @Test
    public void lettersList_whenGetLetters_willReturnJsonArray() throws Exception {
        Letter letter = Letter.builder().to("AlexTo").subject("AlexSubject").text("AlexText").status("SUCCESS").build();
        List<Letter> letters = new LinkedList<>();
        letters.add(letter);
        given(service.findAll()).willReturn(letters);

        mvc.perform(get("/notification")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].to", is(letter.getTo())));
    }

    @Test
    public void lettersList_willReturnEmptyJsonArray() throws Exception {
        List<Letter> letters = new LinkedList<>();
        given(service.findAll()).willReturn(letters);

        mvc.perform(get("/notification")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void sendNotification_willReturnJsonObjectWithValue() throws Exception {
        Message message = new Message();
        message.setTo("slavayus@gmail.com");
        message.setSubject("YEEE");
        message.setText("EEEE");

        UUID uuid = UUID.randomUUID();
        given(service.onSendNotification(message)).willReturn(new Response<>(uuid));

        mvc.perform(post("/notification/send")
                .content(new ObjectMapper().writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(uuid.toString())));
    }


    @Test
    public void sendNotification_withoutText_thenReturnError() throws Exception {
        Message message = new Message();
        message.setTo("slavayus@gmail.com");
        message.setSubject("YEEE");

        mvc.perform(post("/notification/send")
                .content(new ObjectMapper().writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.value", hasSize(1)))
                .andExpect(jsonPath("$.value[0]", is(textMessage)));
    }

    @Test
    public void sendNotification_withoutTextAndTo_thenReturnListOfErrors() throws Exception {
        Message message = new Message();
        message.setSubject("YEEE");

        mvc.perform(post("/notification/send")
                .content(new ObjectMapper().writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.value", hasSize(2)));
    }

    @Test
    public void sendNotification_witLongSubject_thenReturnError() throws Exception {
        Message message = new Message();
        message.setTo("slavayus@gmail.com");
        message.setSubject("YEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        message.setText("EEEE");

        mvc.perform(post("/notification/send")
                .content(new ObjectMapper().writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.value", hasSize(1)))
                .andExpect(jsonPath("$.value[0]", is(subjectMessage)));
    }
}