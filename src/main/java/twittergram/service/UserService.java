package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.User;
import twittergram.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepo;

    public User findByName(String username) {
        return userRepo.findByName(username);
    }
}