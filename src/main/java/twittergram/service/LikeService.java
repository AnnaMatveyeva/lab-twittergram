package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.Like;
import twittergram.entity.Photo;
import twittergram.repository.LikeRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepo;
    private final UserService userService;

    public Like createLike(Photo photo, Long likeOwnerId) {
        Like like = new Like();
        like.setUserId(likeOwnerId);
        like.getPhotos().add(photo);
        return likeRepo.save(like);
    }

    public Like setLike(Photo photo, String likeOwnerNick) {
        Long likeOwnerId = userService.findByNickname(likeOwnerNick).getId();
        Like like = likeRepo.findByUserId(likeOwnerId);
        if (like != null) {
            if (like.getPhotos().contains(photo)) {
                return like;
            } else {
                return addLikePhoto(like, photo);
            }
        } else {
            return createLike(photo, likeOwnerId);
        }
    }

    public Like addLikePhoto(Like like, Photo photo) {
        like.getPhotos().add(photo);
        return likeRepo.save(like);
    }

}
