package twittergram.model;

import lombok.Data;

@Data
public class UserRequestBody {

    private String username;

    private String password;

    private String confirmPass;

    private String email;

}
