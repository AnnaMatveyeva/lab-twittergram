package twittergram.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping
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

    @PutMapping("/{id}")
    public Story update(@PathVariable Long id,
        @RequestBody StoryRequestBody storyRequestBody) {
        return storyService.update(id, storyRequestBody);
    }

    @PostMapping("/like/{id}")
    public Story setLike(@PathVariable Long id, HttpServletRequest request) {
        return storyService
            .addLike(id, userService.findByNickname(request.getRemoteUser()).getId());
    }

    @GetMapping("/{id}")
    public Story getStoryById(@PathVariable Long id) {
        return storyService.findById(id);
    }
}
