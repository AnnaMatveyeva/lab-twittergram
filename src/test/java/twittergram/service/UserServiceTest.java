package twittergram.service;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private PasswordValidatorImpl passwordValidatorImpl;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private StoryService storyService;
    @Mock
    private PhotoService photoService;
    private static final String EMAIL = "email@gmail.com";
    private static final String NICK = "nick";
    private static final String FIRST_NAME = "Oleg";
    private static final String SECOND_NAME = "Olegov";
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findByIdPositiveTest() {
        User preUser = new User();
        preUser.setActive(true);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(preUser));

        User user = userService.findById(1L);
        assertEquals(preUser, user);
        verify(userRepository).findById(1L);
    }

    @Test
    void findByIdNotActiveUserTest() {
        User preUser = new User();
        preUser.setActive(false);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(preUser));

        assertThrows(UserNotActiveException.class, () -> userService.findById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void findByIdNotFoundTest() {
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(0L));
        verify(userRepository).findById(0L);
    }

    @Test
    void findByNicknamePositiveTest() {
        User preUser = new User();
        preUser.setActive(true);
        preUser.setNickname(NICK);
        when(userRepository.findByNickname(NICK)).thenReturn(preUser);

        assertEquals(preUser, userService.findByNickname(NICK));
        verify(userRepository).findByNickname(NICK);
    }

    @Test
    void findByNicknameNotFoundTest() {
        when(userRepository.findByNickname(NICK)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.findByNickname(NICK));
        verify(userRepository).findByNickname(NICK);
    }

    @Test
    void findByEmailPositiveTest() {
        User preUser = new User();
        preUser.setActive(true);
        preUser.setEmail(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(preUser);

        assertEquals(preUser, userService.findByEmail(EMAIL));
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void findByEmailNotFoundTest() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(EMAIL));
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void saveTest() {

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setNickname(NICK);
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setConfirmPass("password");
        userRegistrationDTO.setEmail(EMAIL);

        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userRepository.findByNickname(NICK)).thenReturn(null);
        when(userRepository.findByNickname(EMAIL)).thenReturn(null);
        when(roleRepository.findByName("ROLE_REGULAR")).thenReturn(new Role());
        doCallRealMethod().when(mapper).toEntity(any(), any(), any());
        doCallRealMethod().when(passwordValidatorImpl).arePasswordsMatch(userRegistrationDTO);

        assertNotNull(userService.save(userRegistrationDTO));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateFirstNameTest() {
        User preUser = new User();
        preUser.setActive(true);
        preUser.setNickname(NICK);
        preUser.setFirstName(FIRST_NAME);
        preUser.setId(1L);
        when(userRepository.findByNickname(NICK)).thenReturn(preUser);
        when(userRepository.save(preUser)).thenReturn(preUser);
        doCallRealMethod().when(mapper).toDTO(preUser);

        UserUpdateDTO userUpdateDTO = userService.update(NICK, "newFirstName", null);
        assertNotEquals(FIRST_NAME, userUpdateDTO.getFirstName());
        verify(userRepository).findByNickname(NICK);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateLastNameTest() {
        User preUser = new User();
        preUser.setActive(true);
        preUser.setNickname(NICK);
        preUser.setLastName(FIRST_NAME);
        preUser.setId(1L);
        when(userRepository.findByNickname(NICK)).thenReturn(preUser);
        when(userRepository.save(preUser)).thenReturn(preUser);
        doCallRealMethod().when(mapper).toDTO(preUser);

        UserUpdateDTO userUpdateDTO = userService.update(NICK, null, "newLastName");
        assertNotEquals(SECOND_NAME, userUpdateDTO.getLastName());
        verify(userRepository).findByNickname(NICK);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateNullsTest() {
        User preUser = new User();
        preUser.setActive(true);
        preUser.setNickname(NICK);
        preUser.setLastName(SECOND_NAME);
        preUser.setFirstName(FIRST_NAME);
        preUser.setId(1L);
        when(userRepository.findByNickname(NICK)).thenReturn(preUser);
        when(userRepository.save(preUser)).thenReturn(preUser);
        doCallRealMethod().when(mapper).toDTO(preUser);

        UserUpdateDTO userUpdateDTO = userService.update(NICK, null, null);
        assertEquals(preUser.getFirstName(), userUpdateDTO.getFirstName());
        assertEquals(preUser.getLastName(), userUpdateDTO.getLastName());
        verify(userRepository).findByNickname(NICK);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteTest() {
        User user = new User();
        user.setActive(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.delete(1L);
        verify(userRepository).save(user);
    }
}