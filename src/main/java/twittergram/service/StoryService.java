package twittergram.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import twittergram.entity.Story;
import twittergram.entity.Tag;
import twittergram.exception.StoryNotFoundException;
import twittergram.model.StoryRequestBody;
import twittergram.repository.StoryRepository;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepo;
    private final UserService userService;
    private final TagService tagService;

    public Story findById(Long id) {
        try {
            return storyRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new StoryNotFoundException();
        }
    }

    public Story create(StoryRequestBody storyRequestBody, String nickname) {
        Story story = new Story();
        story.setText(storyRequestBody.getText());
        story.setDate(LocalDate.now());
        story.setUserId(userService.findByNickname(nickname).getId());
        if (!storyRequestBody.getTags().isEmpty()) {
            story = addTags(storyRequestBody.getTags(), storyRepo.save(story));
        }
        return storyRepo.save(story);
    }

    public Story addTags(List<Tag> storyTags, Story story) {
        List<Tag> tags = tagService.saveAll(storyTags);
        if (story.getTags().isEmpty()) {
            story.setTags(tags);
            tagService.addTagsStory(tags, story);
        } else {
            for (Tag tag : tags) {
                if (!story.getTags().contains(tag)) {
                    story.getTags().add(tag);
                    tagService.addTagStory(tag, story);
                }
            }
        }
        return story;
    }

    public Story update(Long storyId, StoryRequestBody storyRequestBody, String nickname) {
        Story story = findById(storyId);
        if (!StringUtils.isEmpty(storyRequestBody.getText())) {
            story.setText(storyRequestBody.getText());
        }
        if (!storyRequestBody.getTags().isEmpty()) {
            addTags(storyRequestBody.getTags(), story);
        }
        return storyRepo.save(story);

    }

}
