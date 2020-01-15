package twittergram.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twittergram.entity.Story;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    Story findByText(String text);

    List<Story> findByTags_Text(String text);

    List<Story> findByDate(LocalDate date);

    List<Story> findByUserId(Long userId);
}
