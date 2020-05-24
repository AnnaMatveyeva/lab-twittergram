package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import twittergram.entity.Like;
import twittergram.entity.Story;
import twittergram.entity.Tag;
import twittergram.exception.StoryNotFoundException;
import twittergram.model.StoryDTO;
import twittergram.repository.StoryRepository;
import twittergram.service.mapper.StoryMapper;
import twittergram.service.specification.StoriesWithAuthor;
import twittergram.service.specification.StoriesWithDate;
import twittergram.service.specification.StoriesWithTag;
import twittergram.service.specification.StoriesWithText;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
		story.setTags(tags);
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
		Like like = likeService.addLike(likeOwnerId);
		if (story.getLikes().contains(like)) {
			return mapper.toDTO(story);
		} else {
			story.getLikes().add(like);
			return mapper.toDTO(storyRepo.save(story));
		}
	}

	public void deleteList(List<Story> stories) {
		for (Story story : stories) {
			delete(story);
		}
	}

	public void deleteUserLikes(Long userId) {
		List<Story> stories = storyRepo.findByLikes_UserId(userId);
		Like byOwnerId = likeService.findByOwnerId(userId);
		if(byOwnerId!=null) {
			stories.forEach(story -> story.getLikes().remove(byOwnerId));
		}
		storyRepo.saveAll(stories);
	}

	public void delete(Story story) {
		storyRepo.delete(story);
	}


	public Page<StoryDTO> findAll(Long userId, String tag, String date, String text,
								  Pageable pageable, Sort sort) {
		Specification specification = new StoriesWithAuthor(userId).and(new StoriesWithTag(tag))
				.and(new StoriesWithDate(date))
				.and(new StoriesWithText(text));
		List<StoryDTO> dtos = new ArrayList<>();
		for (Object entity : storyRepo.findAll(specification, sort)) {
			dtos.add(mapper.toDTO((Story) entity));
		}
		return new PageImpl<StoryDTO>(dtos, pageable, dtos.size());
	}
}
