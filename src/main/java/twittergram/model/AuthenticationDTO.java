package twittergram.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class AuthenticationDTO {

	@NotEmpty
	@Size(min = 4, max = 40)
	private String nickname;

	@NotEmpty
	@Size(min = 4, max = 40)
	private String password;

}