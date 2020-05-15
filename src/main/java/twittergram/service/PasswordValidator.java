package twittergram.service;

import twittergram.model.UserRegistrationDTO;

public interface PasswordValidator {
	public void arePasswordsMatch(UserRegistrationDTO userRegistrationDTO);
}
