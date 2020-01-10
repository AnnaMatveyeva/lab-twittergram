package twittergram.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "stories")
@Data
public class Story extends AbstractEntity {

    private String text;

    private LocalDate date;

    @Column(name = "user_id")
    private Long userId;

}
