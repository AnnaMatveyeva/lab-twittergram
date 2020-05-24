package twittergram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twittergram.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

	Like findByUserId(Long userId);
}
