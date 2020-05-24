package twittergram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import twittergram.model.UserUpdateDTO;
import twittergram.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PutMapping
	public UserUpdateDTO updateUser(@RequestBody @Valid UserUpdateDTO userUpdateDTO,
									HttpServletRequest request)
			throws IOException {

		return userService.update(request.getRemoteUser(), userUpdateDTO.getFirstName(),
				userUpdateDTO.getLastName());
	}

}
