package twittergram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twittergram.service.PhotoService;
import twittergram.service.StoryService;
import twittergram.service.UserService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final StoryService storyService;
    private final PhotoService photoService;

    @DeleteMapping("/user")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "user deleted";
    }

    @DeleteMapping("/story")
    public String deleteStory(@RequestParam Long id) {
        storyService.delete(storyService.findById(id));
        return "story deleted";
    }

    @DeleteMapping("/photo")
    public String deletePhoto(@RequestParam Long id) {
        photoService.delete(photoService.findById(id));
        return "photo deleted";
    }

}
