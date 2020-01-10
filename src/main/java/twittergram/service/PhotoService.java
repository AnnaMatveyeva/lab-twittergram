package twittergram.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.Photo;
import twittergram.repository.PhotoRepository;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepo;
    private final UserService userService;

    public Photo create(byte[] image, String description, String nickname) {
        Photo photo = new Photo();
        photo.setImage(image);
        photo.setDescription(description);
        photo.setUserId(userService.findByNickname(nickname).getId());
        photo.setDate(LocalDate.now());
        return photoRepo.save(photo);
    }

}
