package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.Like;
import twittergram.entity.Photo;
import twittergram.exception.LikeNotFoundException;
import twittergram.repository.LikeRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepo;
    private final UserService userService;
    private final PhotoService photoService;

    public Like setLike(Long photoId, String likeOwnerNick){
        Photo photo;
        try{
             photo = photoService.findByLikeOwner(likeOwnerNick);
             return likeRepo.findByUserId(userService.findByNickname(likeOwnerNick).getId());
        }catch (LikeNotFoundException ex){
            photo = photoService.findById(photoId);
            Like like = new Like();
            like.setUserId(photo.getUserId());
            like = likeRepo.save(like);
            photo.getLikes().add(like);
            photoService.update(photo);

            return like;
        }

    }
}
