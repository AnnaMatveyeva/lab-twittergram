package twittergram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "invalid_tokens")
@Data
public class InvalidToken extends AbstractEntity {

    @Column(name = "token", nullable = false, unique = true, length = 500)
    private String token;

}
