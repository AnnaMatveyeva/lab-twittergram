package twittergram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twittergram.service.PhotoService;
import twittergram.service.StoryService;
import twittergram.service.UserService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final UserService userService;
	private final StoryService storyService;
	private final PhotoService photoService;

	@DeleteMapping("/user/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable Long id, HttpServletResponse response) {
		storyService.deleteList(userService.findById(id).getStories());
		photoService.deleteList(userService.findById(id).getPhotos());
		storyService.deleteUserLikes(id);
		photoService.deleteUserLikes(id);
		userService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}


	@DeleteMapping("/photo/{id}")
	public ResponseEntity deletePhoto(@PathVariable Long id) {
		photoService.delete(photoService.findById(id));
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
