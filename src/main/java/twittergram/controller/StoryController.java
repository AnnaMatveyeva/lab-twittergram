package twittergram.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twittergram.model.StoryDTO;
import twittergram.service.StoryService;
import twittergram.service.UserService;
import twittergram.service.specification.StoriesWithAuthor;
import twittergram.service.specification.StoriesWithDate;
import twittergram.service.specification.StoriesWithTag;
import twittergram.service.specification.StoriesWithText;

@RestController
@RequestMapping("/api/story")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;
    private final UserService userService;

    @PostMapping
    public StoryDTO addStory(@RequestBody @Valid StoryDTO storyDTO,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        if (!StringUtils.isEmpty(storyDTO.getText())) {
            return storyService.create(storyDTO,
                userService.findByNickname(request.getRemoteUser()).getId());
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Text is empty");
            return null;
        }
    }

    @PutMapping("/{id}")
    public StoryDTO update(@PathVariable Long id,
        @RequestBody @Valid StoryDTO storyDTO) {
        return storyService.update(id, storyDTO);
    }

    @PostMapping("/like/{id}")
    public StoryDTO setLike(@PathVariable Long id, HttpServletRequest request) {
        return storyService
            .addLike(id, userService.findByNickname(request.getRemoteUser()).getId());
    }

    @GetMapping("/{id}")
    public StoryDTO getStoryById(@PathVariable Long id) {
        return storyService.findDTOById(id);
    }

    @GetMapping
    public Page<StoryDTO> getPhotos(@RequestParam(required = false) Long userId,
        @RequestParam(required = false) String tag, @RequestParam(required = false) String date,
        @RequestParam(required = false) String text, Pageable pageable, Sort sort) {

        return storyService.findAll(new StoriesWithAuthor(userId).and(new StoriesWithTag(tag))
            .and(new StoriesWithDate(date))
            .and(new StoriesWithText(text)), pageable, sort);
    }
}
