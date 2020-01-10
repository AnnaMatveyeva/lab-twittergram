package twittergram.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "photos")
@Data
public class Photo extends AbstractEntity {

    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;

    private String description;

    private LocalDate date;

    @Column(name = "user_id")
    private Long userId;
}
