package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.Like;
import twittergram.repository.LikeRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepo;

	//метод для добвления Like
	public Like addLike(Long userId) {
		Like byUserId = likeRepo.findByUserId(userId);
		if (byUserId != null) {
			return byUserId;
		}
		Like like = new Like();
		like.setUserId(userId);
		return likeRepo.save(like);
	}

	//метод для поиска Like по id владельца
	public Like findByOwnerId(Long ownerId) {
		Like byUserId = likeRepo.findByUserId(ownerId);
		if (byUserId != null) {
			return byUserId;
		}
		return null;
	}
}
