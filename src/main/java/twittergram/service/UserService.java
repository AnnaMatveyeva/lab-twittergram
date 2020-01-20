package twittergram.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import twittergram.entity.User;
import twittergram.exception.UserNotFoundException;
import twittergram.model.UserDTO;
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
            if (user.isActive()) {
                return user;
            } else {
                throw new UserNotFoundException();
            }
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException();
        }
    }

    public User findByNickname(String nickname) {
        User user = userRepo.findByNickname(nickname);
        if (user != null && user.isActive()) {
            return user;
        } else {
            throw new UserNotFoundException();
        }
    }

    public User findByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null && user.isActive()) {
            return user;
        } else {
            throw new UserNotFoundException();
        }
    }


    public User save(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setNickname(userDTO.getNickname());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(roleRepo.findByName("ROLE_REGULAR"));
        user.setActive(true);
        user.setEmail(userDTO.getEmail());

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
        user.setActive(false);
        userRepo.save(user);

        storyService.deleteList(user.getStories());
        photoService.deleteList(user.getPhotos());
        storyService.deleteUserLikes(id);
        photoService.deleteUserLikes(id);
    }


}