package mailservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "LETTER")
public class Letter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TO")
    private String to;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "STATUS")
    private String status;


    public Letter(String to, String subject, String text, String status) {
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.status = status;
    }

}
