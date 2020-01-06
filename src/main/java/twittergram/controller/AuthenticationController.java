package twittergram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    @PostMapping("/login")
    public String login() {
        return "user logged in";
    }

    @PostMapping("/logout")
    public String logout() {
        return "user logged out";
    }

}
