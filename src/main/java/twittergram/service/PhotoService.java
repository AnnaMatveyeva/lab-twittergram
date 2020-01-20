package twittergram.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import twittergram.entity.Like;
import twittergram.entity.Photo;
import twittergram.entity.Tag;
import twittergram.entity.User;
import twittergram.exception.PhotoNotFoundException;
import twittergram.model.PhotoDTO;
import twittergram.repository.PhotoRepository;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepo;
    private final FileService fileService;
    private final TagService tagService;
    private final LikeService likeService;

    public Photo create(MultipartFile file, User user) {
        Photo photo = new Photo();

        photo.setImage(user.getPhotos().size());
        photo.setUserId(user.getId());
        photo.setDate(LocalDate.now());
        photo.setPath(fileService.uploadFile(file, photo.getImage(), user.getNickname()));
        return photoRepo.save(photo);
    }

    public Photo getByImageAndUserId(int id, Long userId) {
        Photo photo = photoRepo
            .findByImageAndUserId(id, userId);
        if (photo != null) {
            return photo;
        } else {
            throw new PhotoNotFoundException();
        }

    }

    public Photo addPhotoContent(int image, PhotoDTO photoDTO, Long userId) {
        Photo photo = getByImageAndUserId(image, userId);
        if (photo != null) {
            if (!StringUtils.isEmpty(photoDTO.getDescription())) {
                photo.setDescription(photoDTO.getDescription());
            }
            if (!photoDTO.getTags().isEmpty()) {
                tagService.addTags(photoDTO.getTags(), photo);
            }
            if (!StringUtils.isEmpty(photoDTO.getLatitude())) {
                photo.setLatitude(photoDTO.getLatitude());
            }
            if (!StringUtils.isEmpty(photoDTO.getLongitude())) {
                photo.setLongitude(photoDTO.getLongitude());
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

    public Photo addLike(Long photoId, Long likeOwnerId) {
        Photo photo = findById(photoId);
        Like like = likeService.setLike(photo, likeOwnerId);
        if (photo.getLikes().contains(like)) {
            return photo;
        } else {
            photo.getLikes().add(like);
            return photoRepo.save(photo);
        }
    }

    public List<Photo> findByTag(String tag) {
        return photoRepo.findByTags_Text(tag);
    }

    public List<Photo> findByDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return photoRepo.findByDate(localDate);
    }

    public List<Photo> findByAuthor(Long userId) {
        return photoRepo.findByUserId(userId);
    }

    public void deleteList(List<Photo> photos) {
        for (Photo photo : photos) {
            delete(photo);
        }
    }

    public void deleteUserLikes(Long userId) {
        List<Photo> photos = photoRepo.findByLikes_UserId(userId);
        likeService.removeFromPhotos(photos, userId);
        photoRepo.saveAll(photos);
    }

    public void delete(Photo photo) {
        for (Like like : photo.getLikes()) {
            likeService.removePhoto(like, photo);
        }
        for (Tag tag : photo.getTags()) {
            tagService.removePhoto(tag, photo);
        }
        fileService.deleteFile(photo.getPath());
        photoRepo.delete(photo);
    }

    public List<Photo> findByDistance(double longitude, double latitude, double radius) {
        return photoRepo
            .findByLongitudeBetweenAndLatitudeBetween(longitude - radius, longitude + radius,
                latitude - radius, latitude + radius);
    }

    public List<Photo> findByCoordinates(double longitude, double latitude) {
        return photoRepo.findByLongitudeAndLatitude(longitude, latitude);
    }
}
