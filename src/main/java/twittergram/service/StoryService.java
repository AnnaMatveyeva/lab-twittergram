package twittergram.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import twittergram.entity.Like;
import twittergram.entity.Story;
import twittergram.entity.Tag;
import twittergram.exception.StoryNotFoundException;
import twittergram.model.StoryRequestBody;
import twittergram.repository.StoryRepository;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepo;
    private final TagService tagService;
    private final LikeService likeService;

    public Story findById(Long id) {
        try {
            return storyRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new StoryNotFoundException();
        }
    }

    public Story create(StoryRequestBody storyRequestBody, Long userId) {
        Story story = new Story();
        story.setText(storyRequestBody.getText());
        story.setDate(LocalDate.now());
        story.setUserId(userId);
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

    public Story update(Long storyId, StoryRequestBody storyRequestBody) {
        Story story = findById(storyId);
        if (!StringUtils.isEmpty(storyRequestBody.getText())) {
            story.setText(storyRequestBody.getText());
        }
        if (!storyRequestBody.getTags().isEmpty()) {
            addTags(storyRequestBody.getTags(), story);
        }
        return storyRepo.save(story);

    }

    public Story addLike(Long storyId, Long likeOwnerId) {
        Story story = findById(storyId);
        Like like = likeService.setLike(story, likeOwnerId);
        if (story.getLikes().contains(like)) {
            return story;
        } else {
            story.getLikes().add(like);
            return storyRepo.save(story);
        }
    }


    public List<Story> findByTag(String tag) {
        return storyRepo.findByTags_Text(tag);
    }

    public List<Story> findByDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return storyRepo.findByDate(localDate);
    }

    public List<Story> findByAuthor(Long id) {
        return storyRepo.findByUserId(id);
    }

    public void deleteList(List<Story> stories) {
        for (Story story : stories) {
            delete(story);
        }
    }

    public void deleteUserLikes(Long userId) {
        List<Story> stories = storyRepo.findByLikes_UserId(userId);
        likeService.removeFromStories(stories, userId);
        storyRepo.saveAll(stories);
    }

    public void delete(Story story) {
        for (Like like : story.getLikes()) {
            likeService.removeStory(like, story);
        }
        for (Tag tag : story.getTags()) {
            tagService.removeStory(tag, story);
        }
        storyRepo.delete(story);
    }

}
