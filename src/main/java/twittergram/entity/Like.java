package twittergram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "likes")
@Data
public class Like extends AbstractEntity {

    @Column(name = "user_id")
    private Long userId;

}
