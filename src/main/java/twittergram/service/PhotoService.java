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
import twittergram.entity.Photo;
import twittergram.entity.Tag;
import twittergram.entity.User;
import twittergram.exception.PhotoNotFoundException;
import twittergram.model.PhotoCreateDTO;
import twittergram.model.PhotoDTO;
import twittergram.repository.PhotoRepository;
import twittergram.service.mapper.PhotoMapper;
import twittergram.service.specification.PhotoByDistance;
import twittergram.service.specification.PhotosWithAuthor;
import twittergram.service.specification.PhotosWithCoordinates;
import twittergram.service.specification.PhotosWithDate;
import twittergram.service.specification.PhotosWithTag;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {

	private final PhotoRepository photoRepo;
	private final FileService fileService;
	private final TagService tagService;
	private final LikeService likeService;
	private final PhotoMapper mapper;

	public PhotoDTO create(PhotoCreateDTO dto, User user) {
		Photo photo = new Photo();
		photo.setImage(photoRepo.findAll().size() + 1);
		photo.setUserId(user.getId());
		photo.setDate(LocalDate.now());
		photo.setPath(fileService.uploadFile(dto.getFile(), photo.getImage(), user.getNickname()));
		if (!dto.getDescription().isBlank()) {
			photo.setDescription(dto.getDescription());
		}
		if (dto.getTags().length > 0) {
			List<Tag> tags = new ArrayList<>();
			for (String tagText : dto.getTags()){
				Tag tag = new Tag();
				tag.setText(tagText);
				tags.add(tag);
			}
			photo.setTags(tagService.saveAll(tags));
		}
		if (dto.getLatitude()!=null && dto.getLongitude()!=null){
			photo.setLatitude(dto.getLatitude());
			photo.setLongitude(dto.getLongitude());
		}
		return mapper.toDTO(photoRepo.save(photo));
	}

	public Photo findById(Long id) {
		return photoRepo.findById(id).orElseThrow(PhotoNotFoundException::new);
	}

	public PhotoDTO addPhotoContent(Long photoId, PhotoDTO photoDTO, Long userId) {
		Photo photo = findById(photoId);

		if (!StringUtils.isEmpty(photoDTO.getDescription())) {
			photo.setDescription(photoDTO.getDescription());
		}
		if (!photoDTO.getTags().isEmpty()) {
			photo.setTags(tagService.saveAll(photoDTO.getTags()));
		}
		if (!StringUtils.isEmpty(photoDTO.getLatitude())) {
			photo.setLatitude(photoDTO.getLatitude());
		}
		if (!StringUtils.isEmpty(photoDTO.getLongitude())) {
			photo.setLongitude(photoDTO.getLongitude());
		}
		return mapper.toDTO(photoRepo.save(photo));

	}


	public PhotoDTO findDTOById(Long id) {
		return mapper.toDTO(findById(id));
	}

	public PhotoDTO addLike(Long photoId, Long likeOwnerId) {
		Photo photo = findById(photoId);
		Like like = likeService.addLike(likeOwnerId);
		if (photo.getLikes().contains(like)) {
			return mapper.toDTO(photo);
		} else {
			photo.getLikes().add(like);
			return mapper.toDTO(photoRepo.save(photo));
		}
	}

	public void deleteList(List<Photo> photos) {
		for (Photo photo : photos) {
			delete(photo);
		}
	}

	public void deleteUserLikes(Long userId) {
		List<Photo> photos = photoRepo.findByLikes_UserId(userId);
		Like byOwnerId = likeService.findByOwnerId(userId);
		if(byOwnerId!=null){
			photos.forEach(photo -> photo.getLikes().remove(byOwnerId));
		}
		photoRepo.saveAll(photos);
	}

	public void delete(Photo photo) {
		photoRepo.delete(photo);
	}

	public Page<PhotoDTO> findAll(Long userId, String tag, String date, Double longitude,
								  Double latitude, Integer radius, Pageable pageable, Sort sort) {

		Specification specification = new PhotosWithAuthor(userId).and(new PhotosWithTag(tag))
				.and(new PhotosWithDate(date))
				.and(new PhotosWithCoordinates(longitude, latitude, radius))
				.and(new PhotoByDistance(longitude, latitude, radius));

		List<PhotoDTO> dtos = new ArrayList<>();
		for (Object entity : photoRepo.findAll(specification, sort)) {
			dtos.add(mapper.toDTO((Photo) entity));
		}
		return new PageImpl<PhotoDTO>(dtos, pageable, dtos.size());
	}
}
