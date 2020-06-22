package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		List<Photo> all = photoRepo.findAll();
		photo.setImage((int) (all.get(all.size() - 1).getId() + 1));
		photo.setUserId(user.getId());
		photo.setDate(LocalDate.now());
		photo.setPath(fileService.uploadFile(dto.getFile(), photo.getImage(), user.getNickname()));
		if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
			photo.setDescription(dto.getDescription());
		}
		if (dto.getTags() != null && dto.getTags().length > 0) {
			List<Tag> tags = new ArrayList<>();
			for (String tagText : dto.getTags()) {
				Tag tag = new Tag();
				tag.setText(tagText);
				tags.add(tag);
			}
			photo.setTags(tagService.saveAll(tags));
		}
		if (dto.getLatitude() != null && dto.getLongitude() != null) {
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

	public PhotoDTO like(Long photoId, Long likeOwnerId) {
		Photo photo = findById(photoId);
		Like like = likeService.addLike(likeOwnerId);
		if (photo.getLikes().contains(like)) {
			photo.getLikes().remove(like);
			return mapper.toDTO(photoRepo.save(photo));
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
		if (byOwnerId != null) {
			photos.forEach(photo -> photo.getLikes().remove(byOwnerId));
		}
		photoRepo.saveAll(photos);
	}

	public void deleteWithUser(Photo photo,User user) {
		Optional<Photo> one = photoRepo.findOne(Example.of(photo));
		Photo photo1 = one.orElseThrow(PhotoNotFoundException::new);
		if(photo1.getUserId().equals(user.getId()) || user.getRole().getName().equals("ROLE_ADMIN")){
			photoRepo.delete(photo);
			fileService.deleteFile(photo.getPath());
		}else throw new UnsupportedOperationException("You don't have permissions to delete this photo");

	}

	public void delete(Photo photo) {

		photoRepo.delete(photo);
		fileService.deleteFile(photo.getPath());
	}

	public Resource findImageByPhotoId(Long photoId) {
		String path = findById(photoId).getPath();
		File base_path = fileService.base_Path;
		System.out.println(base_path +"\\"+ path);
		return new FileSystemResource(base_path +"\\"+ path);
	}

	public Page<PhotoDTO> findAll(Long userId, String tag, String date, Double longitude,
								  Double latitude, Integer radius, Pageable pageable) {

		Specification specification = new PhotosWithAuthor(userId).and(new PhotosWithTag(tag))
				.and(new PhotosWithDate(date))
				.and(new PhotosWithCoordinates(longitude, latitude, radius))
				.and(new PhotoByDistance(longitude, latitude, radius));

		List<PhotoDTO> dtos = new ArrayList<>();
		Page all = photoRepo.findAll(specification, pageable);
		for (Object entity : all) {
			dtos.add(mapper.toDTO((Photo) entity));
		}
		return new PageImpl<PhotoDTO>(dtos, pageable, all.getTotalElements());
	}
}
