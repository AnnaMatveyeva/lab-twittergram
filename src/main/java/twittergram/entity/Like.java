package twittergram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "likes")
@Data
public class Like extends AbstractEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @JsonIgnore
    @ManyToMany(mappedBy = "likes")
    private List<Photo> photos = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "likes")
    private List<Story> stories = new ArrayList<>();
}
