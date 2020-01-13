package twittergram.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "photos")
@Data
public class Photo extends AbstractEntity {

    private String description;

    @Column(name = "image")
    private int image;

    private String path;

    private LocalDate date;

    @Column(name = "user_id")
    private Long userId;
}
