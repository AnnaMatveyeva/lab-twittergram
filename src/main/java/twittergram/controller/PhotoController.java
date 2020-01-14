package twittergram.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import twittergram.entity.Photo;
import twittergram.model.PhotoRequestBody;
import twittergram.service.PhotoService;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/addImage")
    public String addImage(@RequestParam MultipartFile file, HttpServletRequest request) {
        photoService.create(file, request.getRemoteUser());
        return file.getOriginalFilename();
    }

    @PostMapping("/addPhotoContent")
    public Photo addPhotoContent(@RequestParam int imageId,
        @RequestBody PhotoRequestBody photoRequestBody) {
        return photoService.addPhotoContent(imageId, photoRequestBody);
    }

    @GetMapping(value = "/getImageById")
    public ResponseEntity getImage(@RequestParam int id) {
        Resource fileSystemResource = new FileSystemResource(
            photoService.getByImageId(id).getPath());
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(fileSystemResource);
    }

    @GetMapping(value = "/getPhotosContent")
    public Photo getPhotoContent(@RequestParam int id) {
        return photoService.getByImageId(id);
    }

}
