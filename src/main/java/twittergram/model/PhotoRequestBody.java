package twittergram.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;
import twittergram.entity.Tag;

@Data
public class PhotoRequestBody {

    @Size(max = 150, message = "Description's length should be more than 2 ad less than 150 characters")
    private String description;

    private List<Tag> tags = new ArrayList<>();

}
