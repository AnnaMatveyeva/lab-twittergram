package twittergram.model;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public abstract class AbstractUserDTO extends AbstractDTO {

	@Size(min = 4, max = 40, message = "First name should contain more than 4,but less than 40 characters")
	private String firstName;

	@Size(min = 4, max = 40, message = "Last name should contain more than 4,but less than 40 characters")
	private String lastName;
}
