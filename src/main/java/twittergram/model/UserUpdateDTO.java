package twittergram.model;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @Size(min = 4, max = 32, message = "First name should contain more than 4,but less than 32 characters")
    private String firstName;

    @Size(min = 4, max = 32, message = "Last name should contain more than 4,but less than 32 characters")
    private String lastName;
}
