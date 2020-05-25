package twittergram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twittergram.model.UserUpdateDTO;
import twittergram.service.PhotoService;
import twittergram.service.StoryService;
import twittergram.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final StoryService storyService;
	private final PhotoService photoService;

	@PutMapping
	public UserUpdateDTO updateUser(@RequestBody @Valid UserUpdateDTO userUpdateDTO,
									HttpServletRequest request)
			throws IOException {

		return userService.update(request.getRemoteUser(), userUpdateDTO.getFirstName(),
				userUpdateDTO.getLastName());
	}

	@DeleteMapping
	public ResponseEntity<Object> deleteUser(@RequestParam Long id, HttpServletResponse response) {
		storyService.deleteList(userService.findById(id).getStories());
		photoService.deleteList(userService.findById(id).getPhotos());
		storyService.deleteUserLikes(id);
		photoService.deleteUserLikes(id);
		userService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
