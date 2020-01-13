package twittergram.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twittergram.entity.Photo;
import twittergram.entity.User;
import twittergram.repository.PhotoRepository;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepo;
    private final UserService userService;
    private final FileService fileService;

    public Photo create(MultipartFile file, String description, String nickname) {
        User user = userService.findByNickname(nickname);
        Photo photo = new Photo();

        photo.setImage(user.getPhotos().size());
        photo.setDescription(description);
        photo.setUserId(userService.findByNickname(nickname).getId());
        photo.setDate(LocalDate.now());
        photo.setPath(fileService.uploadFile(file, photo.getImage()));
        return photoRepo.save(photo);
    }

    public Photo getByImageId(int id) {
        return photoRepo.findByImage(id);
    }

}
