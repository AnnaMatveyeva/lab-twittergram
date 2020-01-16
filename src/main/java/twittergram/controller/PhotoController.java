package twittergram.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import twittergram.entity.Photo;
import twittergram.model.PhotoRequestBody;
import twittergram.service.PhotoService;
import twittergram.service.UserService;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
    private final UserService userService;

    @PostMapping("/addImage")
    public String addImage(@RequestParam MultipartFile file, HttpServletRequest request) {
        photoService.create(file, userService.findByNickname(request.getRemoteUser()));
        return file.getOriginalFilename();
    }

    @PostMapping("/addPhotoContent")
    public Photo addPhotoContent(@RequestParam int imageId,
        @RequestBody PhotoRequestBody photoRequestBody, HttpServletRequest request) {
        return photoService.addPhotoContent(imageId, photoRequestBody,
            userService.findByNickname(request.getRemoteUser()).getId());
    }

    @GetMapping(value = "/getUserImage")
    public ResponseEntity getImage(@RequestParam int imageId, HttpServletRequest request) {
        Resource fileSystemResource = new FileSystemResource(
            photoService.getByImageAndUserId(imageId,
                userService.findByNickname(request.getRemoteUser()).getId()).getPath());
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(fileSystemResource);
    }

    @GetMapping(value = "/getPhoto")
    public Photo getPhotoContent(@RequestParam Long photoId) {
        return photoService.findById(photoId);
    }

    @PostMapping("/like")
    public Photo setLike(HttpServletRequest request, @RequestParam Long photoId) {
        return photoService
            .addLike(photoId, userService.findByNickname(request.getRemoteUser()).getId());
    }

}
