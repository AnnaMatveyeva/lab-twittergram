package twittergram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twittergram.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByText(String text);
}
