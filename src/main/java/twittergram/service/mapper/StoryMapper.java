package twittergram.service.mapper;

import org.springframework.stereotype.Component;
import twittergram.entity.Story;
import twittergram.model.StoryDTO;

@Component
public class StoryMapper implements Mapper<Story, StoryDTO> {

	@Override
	public StoryDTO toDTO(Story entity) {
		StoryDTO dto = new StoryDTO();
		dto.setId(entity.getId());
		dto.setText(entity.getText());
		dto.setDate(entity.getDate());
		dto.setUserId(entity.getUserId());
		dto.setTags(entity.getTags());
		dto.setLikes(entity.getLikes());
		return dto;
	}
}
