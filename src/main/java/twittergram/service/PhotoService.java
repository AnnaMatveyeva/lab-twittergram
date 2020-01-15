package twittergram.service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import twittergram.entity.Like;
import twittergram.entity.Photo;
import twittergram.entity.User;
import twittergram.exception.PhotoNotFoundException;
import twittergram.model.PhotoRequestBody;
import twittergram.repository.PhotoRepository;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepo;
    private final UserService userService;
    private final FileService fileService;
    private final TagService tagService;
    private final LikeService likeService;

    public Photo create(MultipartFile file, String nickname) {
        User user = userService.findByNickname(nickname);
        Photo photo = new Photo();

        photo.setImage(user.getPhotos().size());
        photo.setUserId(userService.findByNickname(nickname).getId());
        photo.setDate(LocalDate.now());
        photo.setPath(fileService.uploadFile(file, photo.getImage(), nickname));
        return photoRepo.save(photo);
    }

    public Photo getByImageId(int id, String nickname) {
        Photo photo = photoRepo
            .findByImageAndUserId(id, userService.findByNickname(nickname).getId());
        if (photo != null) {
            return photo;
        } else {
            throw new PhotoNotFoundException();
        }

    }

    public Photo addPhotoContent(int image, PhotoRequestBody photoRequestBody, String nickname) {
        Photo photo = getByImageId(image, nickname);
        if (photo != null) {
            if (!StringUtils.isEmpty(photoRequestBody.getDescription())) {
                photo.setDescription(photoRequestBody.getDescription());
            }
            if (!photoRequestBody.getTags().isEmpty()) {

                tagService.addTags(photoRequestBody.getTags(), photo);
            }
            return photoRepo.save(photo);
        } else {
            throw new PhotoNotFoundException();
        }
    }

    public Photo findById(Long id) {
        try {
            return photoRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new PhotoNotFoundException();
        }
    }

    public Photo addLike(Long photoId, String likeOwnerNick) {
        Photo photo = findById(photoId);
        Like like = likeService.setLike(photo, likeOwnerNick);
        photo.getLikes().add(like);
        return photoRepo.save(photo);
    }
}
