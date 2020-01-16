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

    public Like createLike(Photo photo, Long likeOwnerId) {
        Like like = new Like();
        like.setUserId(likeOwnerId);
        like.getPhotos().add(photo);
        return likeRepo.save(like);
    }

    public Like createLike(Story story, Long likeOwnerId) {
        Like like = new Like();
        like.setUserId(likeOwnerId);
        like.getStories().add(story);
        return likeRepo.save(like);
    }

    public Like setLike(Photo photo, Long likeOwnerId) {
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

    public Like addLikeStory(Like like, Story story) {
        like.getStories().add(story);
        return likeRepo.save(like);
    }

    public Like setLike(Story story, Long likeOwnerId) {
        Like like = likeRepo.findByUserId(likeOwnerId);
        if (like != null) {
            if (like.getStories().contains(story)) {
                return like;
            } else {
                return addLikeStory(like, story);
            }
        } else {
            return createLike(story, likeOwnerId);
        }
    }

    public void removeStory(Like like, Story story) {
        like.getStories().remove(story);
        likeRepo.save(like);
    }

    public void removePhoto(Like like, Photo photo) {
        like.getPhotos().remove(photo);
        likeRepo.save(like);
    }

    public void removeFromStories(List<Story> stories, Long userId) {
        Like like = likeRepo.findByUserId(userId);
        for (Story story : stories) {
            story.getLikes().remove(like);
            like.getStories().remove(story);
        }
        likeRepo.save(like);
    }

    public void removeFromPhotos(List<Photo> photos, Long userId) {
        Like like = likeRepo.findByUserId(userId);
        for (Photo photo : photos) {
            photo.getLikes().remove(like);
            like.getPhotos().remove(photo);
        }
        likeRepo.save(like);
    }

}
