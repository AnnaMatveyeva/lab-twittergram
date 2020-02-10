package twittergram.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import twittergram.exception.UserValidationException;
import twittergram.model.UserRegistrationDTO;

class UserValidatorTest {

    private UserValidator validator;
    private UserRegistrationDTO userRegistrationDTO;
    private final String password = "secret";

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
        userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setPassword(password);
    }

    @Test
    void arePasswordsMatch_negativeTest() {
        userRegistrationDTO.setConfirmPass("pass:" + password);
        assertThrows(UserValidationException.class,
            () -> validator.arePasswordsMatch(userRegistrationDTO));
    }

}