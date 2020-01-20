package twittergram.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import twittergram.entity.Photo;
import twittergram.model.PhotoDTO;
import twittergram.service.PhotoService;
import twittergram.service.UserService;


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
    public Photo addPhotoContent(@PathVariable int imageId,
        @RequestBody PhotoDTO photoDTO, HttpServletRequest request) {
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
    public Photo getPhotoContent(@PathVariable Long id) {
        return photoService.findById(id);
    }

    @PostMapping("/like/{id}")
    public Photo setLike(HttpServletRequest request, @PathVariable Long id) {
        return photoService
            .addLike(id, userService.findByNickname(request.getRemoteUser()).getId());
    }

}
