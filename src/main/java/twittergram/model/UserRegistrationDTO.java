package twittergram.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationDTO extends AbstractUserDTO {

	@NotEmpty(message = "Nickname may not be empty")
	@Size(min = 4, max = 40, message = "Nickname should contain more than 4,but less than 40 characters")
	private String nickname;

	@NotEmpty(message = "Password may not be empty")
	@Size(min = 4, max = 40, message = "Password should contain more than 4,but less than 40 characters")
	private String password;

	@NotEmpty(message = "Confirm password may not be empty")
	private String confirmPass;

	@Email
	@NotEmpty(message = "Email may not be empty")
	private String email;

}
