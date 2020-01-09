package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import twittergram.model.UserRequestBody;

@Service
@RequiredArgsConstructor
public class UserRBService implements Validator {

    private final UserService userService;

    private final String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRequestBody.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRequestBody user = (UserRequestBody) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty");
        if (user.getFirstName().length() < 4 || user.getFirstName().length() > 32) {
            errors.rejectValue("firstName", "Size.userForm.firstName");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty");
        if (user.getFirstName().length() < 4 || user.getFirstName().length() > 32) {
            errors.rejectValue("lastName", "Size.userForm.lastName");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickname", "NotEmpty");
        if (user.getFirstName().length() < 4 || user.getFirstName().length() > 32) {
            errors.rejectValue("nickname", "Size.userForm.nickname");
        }

        if (userService.findByNickname(user.getNickname()) != null) {
            errors.rejectValue("nickname", "Duplicate.userForm.nickname");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        if (!isEmailValid(user.getEmail())) {
            errors.rejectValue("email", "Invalid.userForm.email");
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate.userForm.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 3 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getConfirmPass().equals(user.getPassword())) {
            errors.rejectValue("confirmPass", "Diff.userForm.confirmPass");
        }
    }

    public boolean isEmailValid(String email) {
        if (email.matches(regex)) {
            return true;
        }
        return false;
    }
}
