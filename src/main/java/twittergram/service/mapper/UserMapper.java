package twittergram.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import twittergram.entity.Role;
import twittergram.entity.User;
import twittergram.model.UserRegistrationDTO;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toUser(UserRegistrationDTO dto, PasswordEncoder passwordEncoder, Role role) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setNickname(dto.getNickname());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        user.setActive(true);
        user.setEmail(dto.getEmail());
        return user;
    }
}
