package twittergram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tags")
@Data
public class Tag extends AbstractEntity {

    private String text;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Photo> photos = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Story> stories = new ArrayList<>();


}
