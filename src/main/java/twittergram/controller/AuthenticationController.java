package twittergram.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import twittergram.entity.AuthenticationRequest;
import twittergram.entity.InvalidTokens;
import twittergram.security.JwtTokenProvider;
import twittergram.service.UserService;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequest data,
        HttpServletResponse response)
        throws IOException {
        try {
            String username = data.getUsername();
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username,
                Arrays.asList(userService.findByName(username).getRole()));
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            System.out.println("New token created");
            return ok(model);
        } catch (AuthenticationException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad credentials");
            return null;
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            System.out.println("Token is " + token);
            InvalidTokens.INSTANCE.getTokensList().add(token);
        }
        return "user logged out";
    }

}
