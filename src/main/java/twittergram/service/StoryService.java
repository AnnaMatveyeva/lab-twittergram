package twittergram.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import twittergram.entity.Like;
import twittergram.entity.Story;
import twittergram.entity.Tag;
import twittergram.exception.StoryNotFoundException;
import twittergram.model.StoryDTO;
import twittergram.repository.StoryRepository;
import twittergram.service.mapper.StoryMapper;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepo;
    private final TagService tagService;
    private final LikeService likeService;
    private final StoryMapper mapper;

    public Story findById(Long id) {
        try {
            return storyRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new StoryNotFoundException();
        }
    }

    public StoryDTO findDTOById(Long id) {
        return mapper.toDTO(findById(id));
    }

    public StoryDTO create(StoryDTO storyDTO, Long userId) {
        Story story = new Story();
        story.setText(storyDTO.getText());
        story.setDate(LocalDate.now());
        story.setUserId(userId);
        if (!storyDTO.getTags().isEmpty()) {
            story = addTags(storyDTO.getTags(), storyRepo.save(story));
        }
        return mapper.toDTO(storyRepo.save(story));
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

    public StoryDTO update(Long storyId, StoryDTO storyDTO) {
        Story story = findById(storyId);
        if (!StringUtils.isEmpty(storyDTO.getText())) {
            story.setText(storyDTO.getText());
        }
        if (!storyDTO.getTags().isEmpty()) {
            addTags(storyDTO.getTags(), story);
        }
        return mapper.toDTO(storyRepo.save(story));

    }

    public StoryDTO addLike(Long storyId, Long likeOwnerId) {
        Story story = findById(storyId);
        Like like = likeService.setLike(story, likeOwnerId);
        if (story.getLikes().contains(like)) {
            return mapper.toDTO(story);
        } else {
            story.getLikes().add(like);
            return mapper.toDTO(storyRepo.save(story));
        }
    }


    public List<StoryDTO> findByTag(String tag) {
        List<StoryDTO> dtos = new ArrayList<>();
        for (Story entity : storyRepo.findByTags_Text(tag)) {
            dtos.add(mapper.toDTO(entity));
        }
        return dtos;
    }

    public List<StoryDTO> findByDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        List<StoryDTO> dtos = new ArrayList<>();
        for (Story entity : storyRepo.findByDate(localDate)) {
            dtos.add(mapper.toDTO(entity));
        }
        return dtos;
    }

    public List<StoryDTO> findByAuthor(Long id) {
        List<StoryDTO> dtos = new ArrayList<>();
        for (Story entity : storyRepo.findByUserId(id)) {
            dtos.add(mapper.toDTO(entity));
        }
        return dtos;
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


    public List<Story> getAll() {
        return storyRepo.findAll();
    }

    public List<StoryDTO> findWhichContain(String text) {
        List<Story> allStories = getAll();
        List<StoryDTO> searchResult = new ArrayList<>();
        for (Story story : allStories) {
            if (story.getText().contains(text)) {
                searchResult.add(mapper.toDTO(story));
            }
        }
        return searchResult;
    }
}
