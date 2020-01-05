package twittergram.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
