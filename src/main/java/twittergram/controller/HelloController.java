package twittergram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String getHello() {
        return "This is hello page";
    }

    @GetMapping("/hello-user")
    public String getHelloUser() {
        return "This is hello-user page";
    }

}
