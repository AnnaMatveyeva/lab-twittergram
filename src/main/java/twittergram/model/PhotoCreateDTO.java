package twittergram.model;

import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class PhotoCreateDTO extends AbstractDTO {
	@Nullable
	@Size(max = 150, message = "Description's length should be less than 150 characters")
	private String description;

	@NotNull
	private MultipartFile file;

	@Nullable
	private String[] tags;

	@Nullable
	private Double latitude;

	@Nullable
	private Double longitude;
}
