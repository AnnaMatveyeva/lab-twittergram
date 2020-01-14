package twittergram.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import twittergram.entity.Photo;
import twittergram.entity.Tag;
import twittergram.entity.User;
import twittergram.exception.FileStorageException;
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
        photo.setPath(fileService.uploadFile(file, photo.getImage()));
        return photoRepo.save(photo);
    }

    public Photo getByImageId(int id) {
        return photoRepo.findByImage(id);
    }

    public Photo addPhotoContent(int image, PhotoRequestBody photoRequestBody) {
        Photo photo = photoRepo.findByImage(image);
        if (photo != null) {
            if (!StringUtils.isEmpty(photoRequestBody.getDescription())) {
                photo.setDescription(photoRequestBody.getDescription());
            }
            if (!photoRequestBody.getTags().isEmpty()) {
                List<Tag> tags = tagService.saveAll(photoRequestBody.getTags());
                if (photo.getTags().isEmpty()) {
                    photo.setTags(tags);
                    tagService.addPhotos(tags, photo);
                } else {
                    for (Tag tag : tags) {
                        if (!photo.getTags().contains(tag)) {
                            photo.getTags().add(tag);
                            tagService.addPhoto(tag, photo);
                        }
                    }
                }
            }
            return photoRepo.save(photo);
        } else {
            throw new FileStorageException("Photo not found");
        }
    }
}
