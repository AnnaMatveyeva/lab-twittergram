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
import twittergram.model.AuthenticationRequest;
import twittergram.model.UserRegistrationDTO;
import twittergram.security.JwtTokenProvider;
import twittergram.service.InvalidTokenService;
import twittergram.service.UserService;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final InvalidTokenService invalidTokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequest data,
        HttpServletResponse response)
        throws IOException {
        try {
            String nickname = data.getNickname();
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(nickname, data.getPassword()));
            String token = jwtTokenProvider.createToken(nickname,
                Arrays.asList(userService.findByNickname(nickname).getRole()));
            Map<Object, Object> model = new HashMap<>();
            model.put("nickname", nickname);
            model.put("token", token);
            System.out.println("New token created");
            return ok(model);
        } catch (AuthenticationException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
    }

    @PostMapping("/logoff")
    public ResponseEntity logout(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            invalidTokenService.add(token);
        }
        return ok("User logged out");
    }

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody UserRegistrationDTO userRegistrationDTO,
        HttpServletResponse response)
        throws IOException {
        userService.registrationDTOValidation(userRegistrationDTO);
        return ok(userService.save(userRegistrationDTO));
    }

}
