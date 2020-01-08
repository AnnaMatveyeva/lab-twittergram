package twittergram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getEmptyPath() {
        return "For free";
    }

    @GetMapping("/hello")
    public String getHello() {
        return "This is hello page";
    }

    @GetMapping("/hello-admin")
    public String getHelloAdmin() {
        return "This is admin page";
    }

}
