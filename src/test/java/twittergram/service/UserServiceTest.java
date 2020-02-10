package twittergram.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import twittergram.entity.Role;
import twittergram.entity.User;
import twittergram.exception.UserNotActiveException;
import twittergram.exception.UserNotFoundException;
import twittergram.model.UserRegistrationDTO;
import twittergram.model.UserUpdateDTO;
import twittergram.repository.RoleRepository;
import twittergram.repository.UserRepository;
import twittergram.service.mapper.UserMapper;

class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper mapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private StoryService storyService;
    @Mock
    private PhotoService photoService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findById_positive_test() {
        User preUser = new User();
        preUser.setActive(true);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(preUser));

        User user = userService.findById(1L);
        assertEquals(preUser, user);
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_notActiveUser_test() {
        User preUser = new User();
        preUser.setActive(false);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(preUser));

        assertThrows(UserNotActiveException.class, () -> userService.findById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_notFound_test() {
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(0L));
        verify(userRepository).findById(0L);
    }

    @Test
    void findByNickname_positive_test() {
        User preUser = new User();
        preUser.setActive(true);
        String nick = "nick";
        preUser.setNickname(nick);
        when(userRepository.findByNickname(nick)).thenReturn(preUser);

        assertEquals(preUser, userService.findByNickname(nick));
        verify(userRepository).findByNickname(nick);
    }

    @Test
    void findByNickname_notFound_test() {
        String nick = "nick";
        when(userRepository.findByNickname(nick)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.findByNickname(nick));
        verify(userRepository).findByNickname(nick);
    }

    @Test
    void findByEmail_positive_test() {
        User preUser = new User();
        preUser.setActive(true);
        String email = "email@gmail.com";
        preUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(preUser);

        assertEquals(preUser, userService.findByEmail(email));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_notFound_test() {
        String email = "email@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(email));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void save_test() {

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setNickname("nick");
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setConfirmPass("password");
        userRegistrationDTO.setEmail("email@gmail.com");

        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userRepository.findByNickname("nick")).thenReturn(null);
        when(userRepository.findByNickname("email@gmail.com")).thenReturn(null);
        when(roleRepository.findByName("ROLE_REGULAR")).thenReturn(new Role());
        doCallRealMethod().when(mapper).toEntity(any(), any(), any());
        doCallRealMethod().when(userValidator).arePasswordsMatch(userRegistrationDTO);

        assertNotNull(userService.save(userRegistrationDTO));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_firstName_test() {
        User preUser = new User();
        preUser.setActive(true);
        preUser.setNickname("nick");
        preUser.setFirstName("Oleg");
        preUser.setId(1L);
        when(userRepository.findByNickname("nick")).thenReturn(preUser);
        when(userRepository.save(preUser)).thenReturn(preUser);
        doCallRealMethod().when(mapper).toDTO(preUser);

        UserUpdateDTO userUpdateDTO = userService.update("nick", "newFirstName", null);
        assertNotEquals("Oleg", userUpdateDTO.getFirstName());
        verify(userRepository).findByNickname("nick");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_lastName_test() {
        User preUser = new User();
        preUser.setActive(true);
        preUser.setNickname("nick");
        preUser.setLastName("Olegov");
        preUser.setId(1L);
        when(userRepository.findByNickname("nick")).thenReturn(preUser);
        when(userRepository.save(preUser)).thenReturn(preUser);
        doCallRealMethod().when(mapper).toDTO(preUser);

        UserUpdateDTO userUpdateDTO = userService.update("nick", null, "newLastName");
        assertNotEquals("Olegov", userUpdateDTO.getLastName());
        verify(userRepository).findByNickname("nick");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_nulls_test() {
        User preUser = new User();
        preUser.setActive(true);
        preUser.setNickname("nick");
        preUser.setLastName("Olegov");
        preUser.setFirstName("Oleg");
        preUser.setId(1L);
        when(userRepository.findByNickname("nick")).thenReturn(preUser);
        when(userRepository.save(preUser)).thenReturn(preUser);
        doCallRealMethod().when(mapper).toDTO(preUser);

        UserUpdateDTO userUpdateDTO = userService.update("nick", null, null);
        assertEquals(preUser.getFirstName(), userUpdateDTO.getFirstName());
        assertEquals(preUser.getLastName(), userUpdateDTO.getLastName());
        verify(userRepository).findByNickname("nick");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void delete_test() {
        User user = new User();
        user.setActive(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.delete(1L);
        verify(userRepository).save(user);
    }
}