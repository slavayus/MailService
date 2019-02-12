package mailservice.controllers.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class Message {
    @ApiModelProperty(notes = "To whom to send notification", required = true)
    @NotEmpty(message = "{validation.to.message}")
    @Email(message = "{validation.to.email.message}")
    private String to;
    @ApiModelProperty(notes = "The notification subject")
    @Size(max = 200, message = "{validation.subject.message}")
    private String subject;
    @ApiModelProperty(notes = "The notification text", required = true)
    @NotEmpty(message = "{validation.text.message}")
    private String text;
}
