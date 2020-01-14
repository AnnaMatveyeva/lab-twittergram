package twittergram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twittergram.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findByImageAndUserId(int id, Long userId);
}
