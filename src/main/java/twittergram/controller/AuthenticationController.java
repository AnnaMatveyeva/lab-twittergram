package twittergram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import twittergram.entity.User;
import twittergram.model.AuthenticationDTO;
import twittergram.model.UserRegistrationDTO;
import twittergram.security.JwtTokenProvider;
import twittergram.service.InvalidTokenService;
import twittergram.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final InvalidTokenService invalidTokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data,
        HttpServletResponse response) throws IOException {
        try {
            String nickname = data.getNickname();
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(nickname, data.getPassword()));
            String token = jwtTokenProvider.createToken(nickname,
                Arrays.asList(userService.findByNickname(nickname).getRole()));
            Map<String, String> model = new HashMap<>();
            model.put("nickname", nickname);
            model.put("token", token);
            log.info("New token was created");
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
            log.info("Token was marked as invalid");
        }
        return ok("User logged out");
    }

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO,
        HttpServletResponse response) {
        userService.registrationDTOValidation(userRegistrationDTO);
        Map<String, String> model = new HashMap<>();
        User save = userService.save(userRegistrationDTO);
        log.info("New user was created");
        model.put("nickname",save.getNickname());
        model.put("email", save.getEmail());
        return ok(model);
    }

}
