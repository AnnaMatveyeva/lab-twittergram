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
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService {

	private final StoryRepository storyRepo;
	private final TagService tagService;
	private final LikeService likeService;
	private final StoryMapper mapper;

	//метод для получения историй по id
	public Story findById(Long id) {
		return storyRepo.findById(id).orElseThrow(StoryNotFoundException::new);
	}

	//метод для получения StoryDTO по id
	public StoryDTO findDTOById(Long id) {
		return mapper.toDTO(findById(id));
	}

	//метод для создания историй
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

	//метод для добавления тегэв к истории
	public Story addTags(List<Tag> storyTags, Story story) {
		List<Tag> tags = tagService.saveAll(storyTags);
		story.setTags(tags);
		return story;
	}

	//метод для редактирования историй
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

	//метод для добавления к истории
	public StoryDTO like(Long storyId, Long likeOwnerId) {
		Story story = findById(storyId);
		Like like = likeService.addLike(likeOwnerId);
		if (story.getLikes().contains(like)) {
			story.getLikes().remove(like);
			return mapper.toDTO(storyRepo.save(story));
		} else {
			story.getLikes().add(like);
			return mapper.toDTO(storyRepo.save(story));
		}
	}

	//метод для удаления списка историй
	public void deleteList(List<Story> stories) {
		for (Story story : stories) {
			delete(story);
		}
	}

	//метод для удаления лайка из историй
	public void deleteUserLikes(Long userId) {
		List<Story> stories = storyRepo.findByLikes_UserId(userId);
		Like byOwnerId = likeService.findByOwnerId(userId);
		if (byOwnerId != null) {
			stories.forEach(story -> story.getLikes().remove(byOwnerId));
		}
		storyRepo.saveAll(stories);
	}

	//метод для удаления историй
	public void delete(Story story) {
		storyRepo.delete(story);
	}

	//метод для поиска историй
	public Page<StoryDTO> findAll(Long userId, String tag, String date, String text,
								  Pageable pageable) {
		Specification specification = new StoriesWithAuthor(userId).and(new StoriesWithTag(tag))
				.and(new StoriesWithDate(date))
				.and(new StoriesWithText(text));
		List<StoryDTO> dtos = new ArrayList<>();
		Page all = storyRepo.findAll(specification, pageable);
		for (Object entity : all) {
			dtos.add(mapper.toDTO((Story) entity));
		}
		return new PageImpl<StoryDTO>(dtos, pageable,all.getTotalElements());
	}
}
