package twittergram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twittergram.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByNickname(String nickname);

	User findByEmail(String email);
}
