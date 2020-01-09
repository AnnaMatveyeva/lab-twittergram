package twittergram.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import twittergram.entity.User;
import twittergram.model.UserRequestBody;
import twittergram.service.UserRBService;
import twittergram.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRBService userRBService;

    @PutMapping("/user")
    public User updateUser(@RequestBody UserRequestBody userRequestBody,
        BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request)
        throws IOException {
        userRBService.validate(userRequestBody, bindingResult);
        if (bindingResult.hasErrors()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid data");
        }
        return userService
            .update(request.getRemoteUser(), userRequestBody.getFirstName(),
                userRequestBody.getLastName());
    }

}
