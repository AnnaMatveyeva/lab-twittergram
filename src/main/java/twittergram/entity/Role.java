package twittergram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity {

    @Column(name = "name", nullable = false, length = 255, unique = true)
    private String name;
}
