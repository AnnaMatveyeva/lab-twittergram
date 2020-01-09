package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import twittergram.entity.User;
import twittergram.model.UserRequestBody;
import twittergram.repository.RoleRepository;
import twittergram.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public User findByNickname(String nickname) {
        return userRepo.findByNickname(nickname);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User save(UserRequestBody userRequestBody) {
        User user = new User();
        user.setFirstName(userRequestBody.getFirstName());
        user.setLastName(userRequestBody.getLastName());
        user.setNickname(userRequestBody.getNickname());
        user.setPassword(passwordEncoder.encode(userRequestBody.getPassword()));
        user.setRole(roleRepo.findByName("ROLE_REGULAR"));
        user.setEmail(userRequestBody.getEmail());

        return userRepo.save(user);
    }
}