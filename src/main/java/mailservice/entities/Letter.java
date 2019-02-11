package mailservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "LETTER")
public class Letter {
    @Id
    @Column(columnDefinition = "uuid", name = "LETTER_UUID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;

    @Column(name = "TO")
    private String to;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "STATUS")
    private String status;


    public Letter(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}
