package twittergram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twittergram.model.PhotoCreateDTO;
import twittergram.model.PhotoDTO;
import twittergram.service.PhotoService;
import twittergram.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {

	private final PhotoService photoService;
	private final UserService userService;

	@PostMapping
	public PhotoDTO addPhoto(@ModelAttribute @Valid PhotoCreateDTO photoDTO, HttpServletRequest request) {

		return photoService.create(photoDTO, userService.findByNickname(request.getRemoteUser()));
	}

	@PutMapping("/{photoId}")
	public PhotoDTO updatePhotoContent(@PathVariable Long photoId,
									   @RequestBody @Valid PhotoDTO photoDTO, HttpServletRequest request) {
		return photoService.addPhotoContent(photoId, photoDTO,
				userService.findByNickname(request.getRemoteUser()).getId());
	}

	@GetMapping("/image/{photoId}")
	public ResponseEntity getImage(@PathVariable Long photoId, HttpServletRequest request) {
		Resource fileSystemResource = photoService.findImageByPhotoId(photoId);
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(fileSystemResource);
	}

	@GetMapping("/{id}")
	public PhotoDTO getPhotoContent(@PathVariable Long id) {
		return photoService.findDTOById(id);
	}

	@PostMapping("/like/{id}")
	public PhotoDTO setLike(HttpServletRequest request, @PathVariable Long id) {
		return photoService
				.addLike(id, userService.findByNickname(request.getRemoteUser()).getId());
	}

	@GetMapping
	public Page<PhotoDTO> getPhotos(@RequestParam(required = false) Long userId,
									@RequestParam(required = false) String tag, @RequestParam(required = false) String date,
									@RequestParam(required = false) Double longitude,
									@RequestParam(required = false) Double latitude,
									@RequestParam(required = false) Integer radius, Sort sort, Pageable pageable) {

		return photoService.findAll(userId, tag, date, longitude, latitude, radius, pageable, sort);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity deletePhoto(@PathVariable Long id) {
		photoService.delete(photoService.findById(id));
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
