package twittergram.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import twittergram.entity.Photo;
import twittergram.model.PhotoRequestBody;
import twittergram.service.PhotoService;

@RestController
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/photo")
    public Photo create(@RequestBody PhotoRequestBody photoRequestBody,
        HttpServletRequest request) {
        return photoService.create(photoRequestBody.getImage(), photoRequestBody.getDescription(),
            request.getRemoteUser());
    }
}
