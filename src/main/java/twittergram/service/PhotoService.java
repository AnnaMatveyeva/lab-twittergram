package twittergram.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import twittergram.entity.Photo;
import twittergram.entity.Tag;
import twittergram.entity.User;
import twittergram.exception.LikeNotFoundException;
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
                List<Tag> tags = tagService.saveAll(photoRequestBody.getTags());
                if (photo.getTags().isEmpty()) {
                    photo.setTags(tags);
                    tagService.addTagsPhoto(tags, photo);
                } else {
                    for (Tag tag : tags) {
                        if (!photo.getTags().contains(tag)) {
                            photo.getTags().add(tag);
                            tagService.addTagPhoto(tag, photo);
                        }
                    }
                }
            }
            return photoRepo.save(photo);
        } else {
            throw new PhotoNotFoundException();
        }
    }

    public Photo findByLikeOwner(String likeOwnerNick){
        Photo photo = photoRepo
            .findByLikesUserId(userService.findByNickname(likeOwnerNick).getId());
        if (photo != null) {
            return photo;
        } else {
            throw new LikeNotFoundException();
        }
    }

    public Photo findById(Long id) {
        try {
            return photoRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new PhotoNotFoundException();
        }
    }

    public Photo update(Photo photo){
        return photoRepo.save(photo);
    }
}
