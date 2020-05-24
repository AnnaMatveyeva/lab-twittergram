package twittergram.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.Like;
import twittergram.entity.Photo;
import twittergram.entity.Story;
import twittergram.repository.LikeRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepo;

    public Like addLike(Long userId){
        Like byUserId = likeRepo.findByUserId(userId);
        if(byUserId!=null){
            return byUserId;
        }
        Like like = new Like();
        like.setUserId(userId);
        return likeRepo.save(like);
    }

    public Like findByOwnerId(Long ownerId){
        Like byUserId = likeRepo.findByUserId(ownerId);
        if(byUserId!=null){
            return byUserId;
        }
        return null;
    }

}
