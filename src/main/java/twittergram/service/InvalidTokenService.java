package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.InvalidToken;
import twittergram.repository.InvalidTokenRepository;

@Service
@RequiredArgsConstructor
public class InvalidTokenService {

    private InvalidTokenRepository invalidTokenRepo;

    public InvalidToken findByToken(String token) {
        return invalidTokenRepo.findByToken(token);
    }

}
