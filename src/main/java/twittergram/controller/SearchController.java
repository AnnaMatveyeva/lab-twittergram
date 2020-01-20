package twittergram.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twittergram.entity.Photo;
import twittergram.entity.Story;
import twittergram.service.PhotoService;
import twittergram.service.StoryService;
import twittergram.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final PhotoService photoService;
    private final StoryService storyService;
    private final UserService userService;

    @GetMapping("/photos-by-tag")
    public List<Photo> findPhotoByTag(@RequestParam String tag) {
        return photoService.findByTag(tag);
    }

    @GetMapping("/photos-by-date")
    public List<Photo> findPhotoByDate(@RequestParam String date) {
        return photoService.findByDate(date);
    }

    @GetMapping("/photos-by-author")
    public List<Photo> findPhotoByAuthor(@RequestParam String nickname) {
        return photoService.findByAuthor(userService.findByNickname(nickname).getId());
    }

    @GetMapping("/stories-by-tag")
    public List<Story> findStoriesByTag(@RequestParam String tag) {
        return storyService.findByTag(tag);
    }

    @GetMapping("/stories-by-date")
    public List<Story> findStoriesByDate(@RequestParam String date) {
        return storyService.findByDate(date);
    }

    @GetMapping("/stories-by-author")
    public List<Story> findStoriesByAuthor(@RequestParam String nickname) {
        return storyService.findByAuthor(userService.findByNickname(nickname).getId());
    }

    @GetMapping("/stories-by-text")
    public List<Story> findStoriesByText(@RequestParam String text) {
        return storyService.findWhichContain(text);
    }
}
