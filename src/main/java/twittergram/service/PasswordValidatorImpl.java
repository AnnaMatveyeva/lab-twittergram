package twittergram.service;

import org.springframework.stereotype.Component;
import twittergram.exception.UserValidationException;
import twittergram.model.UserRegistrationDTO;

@Component
public class PasswordValidatorImpl implements PasswordValidator {

	@Override
	public void arePasswordsMatch(UserRegistrationDTO userRegistrationDTO) {
		if (!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPass())) {
			throw new UserValidationException("Passwords don't match");
		}

	}
}
