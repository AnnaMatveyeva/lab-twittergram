package twittergram.model;

import lombok.Data;

@Data
public class UserRequestBody {

    private String nickname;

    private String firstName;

    private String lastName;

    private String password;

    private String confirmPass;

    private String email;

}
