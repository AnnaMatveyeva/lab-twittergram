package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.InvalidToken;
import twittergram.repository.InvalidTokenRepository;

@Service
@RequiredArgsConstructor
public class InvalidTokenService {

    private final InvalidTokenRepository invalidTokenRepo;

    public InvalidToken findByToken(String token) {
        return invalidTokenRepo.findByToken(token);
    }

    public void add(String token) {
        InvalidToken invalidToken = new InvalidToken();
        invalidToken.setToken(token);
        invalidTokenRepo.save(invalidToken);
    }

}
