package twittergram.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.io.UnsupportedEncodingException;
import lombok.Data;
import org.bouncycastle.util.encoders.Base64;

@Data
public class PhotoRequestBody {

    private byte[] image;
    private String description;

    @JsonSetter("image")
    public void setImage(String image) throws UnsupportedEncodingException {
        this.image = Base64.decode(image.getBytes("UTF-8"));
    }
}
