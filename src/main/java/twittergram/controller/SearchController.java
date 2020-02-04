package twittergram.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twittergram.model.PhotoDTO;
import twittergram.model.StoryDTO;
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
    public List<PhotoDTO> findPhotoByTag(@RequestParam String tag) {
        return photoService.findByTag(tag);
    }

    @GetMapping("/photos-by-date")
    public List<PhotoDTO> findPhotoByDate(@RequestParam String date) {
        return photoService.findByDate(date);
    }

    @GetMapping("/photos-by-author")
    public List<PhotoDTO> findPhotoByAuthor(@RequestParam String nickname) {
        return photoService.findByAuthor(userService.findByNickname(nickname).getId());
    }

    @GetMapping("/stories-by-tag")
    public List<StoryDTO> findStoriesByTag(@RequestParam String tag) {
        return storyService.findByTag(tag);
    }

    @GetMapping("/stories-by-date")
    public List<StoryDTO> findStoriesByDate(@RequestParam String date) {
        return storyService.findByDate(date);
    }

    @GetMapping("/stories-by-author")
    public List<StoryDTO> findStoriesByAuthor(@RequestParam String nickname) {
        return storyService.findByAuthor(userService.findByNickname(nickname).getId());
    }

    @GetMapping("/stories-by-text")
    public List<StoryDTO> findStoriesByText(@RequestParam String text) {
        return storyService.findWhichContain(text);
    }

    @GetMapping("/photos-by-distance")
    public List<PhotoDTO> findPhotosByDistance(@RequestParam double latitude,
        @RequestParam double longitude, @RequestParam int radius) {
        return photoService.findByDistance(longitude, latitude, radius);
    }

    @GetMapping("/photos-by-coordinates")
    public List<PhotoDTO> findPhotosByCoordinate(@RequestParam double latitude,
        @RequestParam double longitude) {
        return photoService.findByCoordinates(longitude, latitude);
    }
}
