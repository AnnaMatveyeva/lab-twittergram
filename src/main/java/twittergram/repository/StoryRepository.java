package twittergram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twittergram.entity.Story;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    Story findByText(String text);
}
