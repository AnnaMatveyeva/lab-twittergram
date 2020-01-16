package twittergram.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twittergram.entity.Story;
import twittergram.model.StoryRequestBody;
import twittergram.service.StoryService;
import twittergram.service.UserService;

@RestController
@RequestMapping("/api/story")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;
    private final UserService userService;

    @PostMapping("/addStory")
    public Story addStory(@RequestBody StoryRequestBody storyRequestBody,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        if (!StringUtils.isEmpty(storyRequestBody.getText())) {
            return storyService.create(storyRequestBody,
                userService.findByNickname(request.getRemoteUser()).getId());
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Text is empty");
            return null;
        }
    }

    @PutMapping("/update")
    public Story update(@RequestParam Long storyId,
        @RequestBody StoryRequestBody storyRequestBody) {
        return storyService.update(storyId, storyRequestBody);
    }

    @PostMapping("/like")
    public Story setLike(@RequestParam Long storyId, HttpServletRequest request) {
        return storyService
            .addLike(storyId, userService.findByNickname(request.getRemoteUser()).getId());
    }

    @GetMapping("/getStory")
    public Story getStoryById(@RequestParam Long storyId) {
        return storyService.findById(storyId);
    }
}
