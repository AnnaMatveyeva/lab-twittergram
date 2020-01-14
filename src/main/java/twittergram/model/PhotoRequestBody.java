package twittergram.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import twittergram.entity.Tag;

@Data
public class PhotoRequestBody {

    private String description;

    private List<Tag> tags = new ArrayList<>();

}
