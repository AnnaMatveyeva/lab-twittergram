package twittergram.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import twittergram.model.PhotoDTO;
import twittergram.service.PhotoService;
import twittergram.service.UserService;
import twittergram.service.specification.PhotoByDistance;
import twittergram.service.specification.PhotosWithAuthor;
import twittergram.service.specification.PhotosWithCoordinates;
import twittergram.service.specification.PhotosWithDate;
import twittergram.service.specification.PhotosWithTag;


@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
    private final UserService userService;

    @PostMapping("/image")
    public String addImage(@RequestParam MultipartFile file, HttpServletRequest request) {
        photoService.create(file, userService.findByNickname(request.getRemoteUser()));

        return file.getOriginalFilename();
    }

    @PutMapping("/{imageId}")
    public PhotoDTO addPhotoContent(@PathVariable int imageId,
        @RequestBody @Valid PhotoDTO photoDTO, HttpServletRequest request) {
        return photoService.addPhotoContent(imageId, photoDTO,
            userService.findByNickname(request.getRemoteUser()).getId());
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity getImage(@PathVariable int imageId, HttpServletRequest request) {
        Resource fileSystemResource = new FileSystemResource(
            photoService.getByImageAndUserId(imageId,
                userService.findByNickname(request.getRemoteUser()).getId()).getPath());
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

        return photoService.findAll(
            new PhotosWithAuthor(userId).and(new PhotosWithTag(tag)).and(new PhotosWithDate(date))
                .and(new PhotosWithCoordinates(longitude, latitude, radius))
                .and(new PhotoByDistance(longitude, latitude, radius)), pageable, sort);
    }

}
