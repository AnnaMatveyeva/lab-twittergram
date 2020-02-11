package twittergram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import twittergram.entity.InvalidToken;
import twittergram.repository.InvalidTokenRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvalidTokenService {

    private final InvalidTokenRepository invalidTokenRepo;

    public boolean isExists(String token) {
        InvalidToken tokenFromDb = invalidTokenRepo.findByToken(token);
        if (tokenFromDb != null) {
            return true;
        } else {
            return false;
        }
    }

    public void add(String token) {
        InvalidToken invalidToken = new InvalidToken();
        invalidToken.setToken(token);
        invalidTokenRepo.save(invalidToken);
        log.trace("Token" + token + " became invalid");
    }

}
