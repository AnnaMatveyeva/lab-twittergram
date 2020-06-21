package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import twittergram.entity.InvalidToken;
import twittergram.repository.InvalidTokenRepository;

@Service
@RequiredArgsConstructor
public class InvalidTokenService {

	private final InvalidTokenRepository invalidTokenRepo;
	private static final Logger log = LoggerFactory.getLogger(InvalidTokenService.class);


	public boolean isExists(String token) {
		InvalidToken tokenFromDb = invalidTokenRepo.findByToken(token);
		return tokenFromDb != null;
	}

	public void add(String token) {
		InvalidToken invalidToken = new InvalidToken();
		invalidToken.setToken(token);
		invalidTokenRepo.save(invalidToken);
		log.debug("Token {} became invalid", token);
	}

}
