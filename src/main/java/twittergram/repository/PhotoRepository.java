package twittergram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import twittergram.entity.Photo;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>,
		JpaSpecificationExecutor<Photo> {

	Photo findByImageAndUserId(int id, Long userId);

	List<Photo> findByLikes_UserId(Long userId);
}
