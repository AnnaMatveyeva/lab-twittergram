package twittergram.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import twittergram.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>,
    JpaSpecificationExecutor<Photo> {

    Photo findByImageAndUserId(int id, Long userId);

    List<Photo> findByLikes_UserId(Long userId);
}
