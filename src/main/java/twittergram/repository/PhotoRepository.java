package twittergram.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twittergram.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findByImageAndUserId(int id, Long userId);

    List<Photo> findByTags_Text(String text);

    List<Photo> findByDate(LocalDate date);

    List<Photo> findByUserId(Long userId);

    List<Photo> findByLikes_UserId(Long userId);

    List<Photo> findByLongitudeBetweenAndLatitudeBetween(double fromLong, double toLong, double fromLat, double toLat);


}
