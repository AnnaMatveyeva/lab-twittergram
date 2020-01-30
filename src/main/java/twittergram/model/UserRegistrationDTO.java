package twittergram.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    @NotEmpty(message = "Nickname may not be empty")
    @Size(min = 4, max = 32, message = "Nickname should contain more than 4,but less than 32 characters")
    private String nickname;

    @NotEmpty(message = "First name may not be empty")
    @Size(min = 4, max = 32, message = "First name should contain more than 4,but less than 32 characters")
    private String firstName;

    @NotEmpty(message = "Last name may not be empty")
    @Size(min = 4, max = 32, message = "Last name should contain more than 4,but less than 32 characters")
    private String lastName;

    @NotEmpty(message = "Password may not be empty")
    @Size(min = 4, max = 32, message = "Password should contain more than 4,but less than 32 characters")
    private String password;

    @NotEmpty(message = "Confirm password may not be empty")
    private String confirmPass;

    @Email
    @NotEmpty(message = "Email may not be empty")
    private String email;

}
