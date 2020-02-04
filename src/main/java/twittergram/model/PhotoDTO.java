package twittergram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;
import twittergram.entity.Like;
import twittergram.entity.Tag;

@Data
public class PhotoDTO extends AbstractDTO {

    @Size(max = 150, message = "Description's length should be less than 150 characters")
    private String description;

    private List<Tag> tags = new ArrayList<>();

    private List<Like> likes = new ArrayList<>();

    private double latitude = 0;

    private double longitude = 0;

    @JsonProperty(access = Access.READ_ONLY)
    private int image;
    @JsonProperty(access = Access.READ_ONLY)
    private String path;
    @JsonProperty(access = Access.READ_ONLY)
    private LocalDate date;
    @JsonProperty(access = Access.READ_ONLY)
    private Long userId;
}
