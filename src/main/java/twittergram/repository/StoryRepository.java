package twittergram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import twittergram.entity.Story;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long>,
		JpaSpecificationExecutor<Story> {

	List<Story> findByLikes_UserId(Long userId);
}
