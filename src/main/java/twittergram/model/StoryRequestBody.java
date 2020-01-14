package twittergram.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;
import twittergram.entity.Tag;

@Data
public class StoryRequestBody {

    @Size(min = 2, max = 150, message = "Story's length should be more than 2 ad less than 150 characters")
    private String text;

    private List<Tag> tags = new ArrayList<>();
}
