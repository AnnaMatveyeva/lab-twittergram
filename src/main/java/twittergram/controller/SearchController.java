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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final PhotoService photoService;
    private final StoryService storyService;

    @GetMapping("/photosByTag")
    public List<Photo> findPhotoByTag(@RequestParam String tag) {
        return photoService.findByTag(tag);
    }

    @GetMapping("/photosByDate")
    public List<Photo> findPhotoByDate(@RequestParam String date) {
        return photoService.findByDate(date);
    }

    @GetMapping("/photosByAuthor")
    public List<Photo> findPhotoByAuthor(@RequestParam String nickname) {
        return photoService.findByAuthor(nickname);
    }

    @GetMapping("/storiesByTag")
    public List<Story> findStoriesByTag(@RequestParam String tag) {
        return storyService.findByTag(tag);
    }

    @GetMapping("/storiesByDate")
    public List<Story> findStoriesByDate(@RequestParam String date) {
        return storyService.findByDate(date);
    }

    @GetMapping("/storiesByAuthor")
    public List<Story> findStoriesByAuthor(@RequestParam String nickname) {
        return storyService.findByAuthor(nickname);
    }

}
