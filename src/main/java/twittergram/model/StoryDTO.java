package twittergram.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Max;
import lombok.Data;
import twittergram.entity.Tag;

@Data
public class StoryDTO {

    @Max(value = 150, message = "Story's length should be less than 150 characters")
    private String text;

    private List<Tag> tags = new ArrayList<>();
}
