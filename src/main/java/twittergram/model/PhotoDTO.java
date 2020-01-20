package twittergram.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Max;
import lombok.Data;
import twittergram.entity.Tag;

@Data
public class PhotoDTO {

    @Max(value = 150, message = "Description's length should be less than 150 characters")
    private String description;

    private List<Tag> tags = new ArrayList<>();

    private double latitude;

    private double longitude;

}
