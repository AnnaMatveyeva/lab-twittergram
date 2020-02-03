package twittergram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import twittergram.entity.Like;
import twittergram.entity.Tag;

@Data
public class StoryDTO extends AbstractDTO {

    @NotEmpty
    @Size(max = 150, message = "Story's length should be less than 150 characters")
    private String text;

    private List<Tag> tags = new ArrayList<>();

    private List<Like> likes = new ArrayList<>();

    @JsonProperty(access = Access.READ_ONLY)
    private Long userId;

    @JsonProperty(access = Access.READ_ONLY)
    private LocalDate date;
}
