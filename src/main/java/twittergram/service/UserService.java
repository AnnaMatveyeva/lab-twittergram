package twittergram.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import twittergram.entity.User;
import twittergram.exception.UserNotFoundException;
import twittergram.model.UserRequestBody;
import twittergram.repository.RoleRepository;
import twittergram.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final StoryService storyService;
    private final PhotoService photoService;

    public User findById(Long id) {
        try {
            User user = userRepo.findById(id).get();
            if (user.getStatus().equals("ACTIVE")) {
                return user;
            } else {
                throw new UserNotFoundException();
            }
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException();
        }
    }

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
        user.setStatus("ACTIVE");
        user.setEmail(userRequestBody.getEmail());

        return userRepo.save(user);
    }

    public User update(String nickname, String firstName, String lastName) {
        User user = findByNickname(nickname);
        if (!StringUtils.isEmpty(firstName)) {
            user.setFirstName(firstName);
        }
        if (!StringUtils.isEmpty(lastName)) {
            user.setLastName(lastName);
        }
        return userRepo.save(user);
    }

    public void delete(Long id) {
        User user = findById(id);
        user.setStatus("DELETED");
        userRepo.save(user);

        storyService.deleteList(user.getStories());
        photoService.deleteList(user.getPhotos());
        storyService.deleteUserLikes(id);
        photoService.deleteUserLikes(id);
    }


}