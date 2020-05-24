package twittergram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;

@Data
public class AbstractDTO {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
}
