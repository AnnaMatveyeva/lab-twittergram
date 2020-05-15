package twittergram.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import twittergram.exception.UserValidationException;
import twittergram.model.UserRegistrationDTO;

class UserPasswordValidatorTest {

    private PasswordValidatorImpl validator;
    private UserRegistrationDTO userRegistrationDTO;
    private static final String PASSWORD = "secret";

    @BeforeEach
    void setUp() {
        validator = new PasswordValidatorImpl();
        userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setPassword(PASSWORD);
    }

    @Test
    void arePasswordsMatchNegativeTest() {
        userRegistrationDTO.setConfirmPass("pass:" + PASSWORD);
        assertThrows(UserValidationException.class,
            () -> validator.arePasswordsMatch(userRegistrationDTO));
    }

}