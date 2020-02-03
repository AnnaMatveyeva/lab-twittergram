package twittergram.service.mapper;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import twittergram.entity.Photo;
import twittergram.model.PhotoDTO;

@Component
public class PhotoMapper implements Mapper<Photo, PhotoDTO> {

    @Override
    public PhotoDTO toDTO(Photo entity) {
        PhotoDTO dto = new PhotoDTO();
        dto.setId(entity.getId());
        if (!StringUtils.isEmpty(entity.getDescription())) {
            dto.setDescription(entity.getDescription());
        }
        dto.setTags(entity.getTags());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setDate(entity.getDate());
        dto.setImage(entity.getImage());
        dto.setLikes(entity.getLikes());
        dto.setPath(entity.getPath());
        dto.setUserId(entity.getUserId());
        return dto;
    }
}
