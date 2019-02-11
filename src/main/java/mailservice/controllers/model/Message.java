package mailservice.controllers.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class Message {
    @NotEmpty(message = "{validation.to.message}")
    private String to;
    @Size(max = 200, message = "{validation.subject.message}")
    private String subject;
    @NotEmpty(message = "{validation.text.message}")
    private String text;
}
