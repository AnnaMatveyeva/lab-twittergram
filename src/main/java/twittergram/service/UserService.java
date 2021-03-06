package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import twittergram.entity.Role;
import twittergram.entity.User;
import twittergram.exception.UserNotActiveException;
import twittergram.exception.UserNotFoundException;
import twittergram.exception.UserValidationException;
import twittergram.model.UserRegistrationDTO;
import twittergram.model.UserUpdateDTO;
import twittergram.repository.RoleRepository;
import twittergram.repository.UserRepository;
import twittergram.service.mapper.UserMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper mapper;
	private final PasswordValidator passwordValidator;
	@Value("${application.company.email-domain}")
	private String emailDomain;

	public User findById(Long id) {

		Optional<User> op = userRepo.findById(id);
		User user = op.orElseThrow(UserNotFoundException::new);
		if (user.isActive()) {
			return user;
		} else {
			throw new UserNotActiveException("User " + user.getNickname() + " has been deleted");
		}
	}

	public List<Map<String, String>> findAllForAdmin() {
		return findAll().stream()
				.filter(user -> !user.getRole().getName().contains("ADMIN"))
				.map(user -> {
					final Map<String, String> model = new LinkedHashMap<>();
					model.put("id", user.getId().toString());
					model.put("nickname", user.getNickname());
					model.put("email", user.getEmail());
					model.put("firstName", user.getFirstName());
					model.put("lastName", user.getLastName());
					model.put("Num_of_stories", String.valueOf(user.getStories().size()));
					model.put("Num_of_photos", String.valueOf(user.getPhotos().size()));
					model.put("isActive", String.valueOf(user.isActive()));
					return model;
				}).collect(Collectors.toList());
	}

	public List<User> findAll() {
		return userRepo.findAll();
	}

	public User findByNickname(String nickname) {
		User user = userRepo.findByNickname(nickname);
		if (user != null && user.isActive()) {
			return user;
		} else {
			throw new UserNotFoundException();
		}
	}

	private User findAnyActive(String nickname) {
		User user = userRepo.findByNickname(nickname);
		if (user != null) {
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


	public User save(UserRegistrationDTO userRegistrationDTO) {
		registrationDTOValidation(userRegistrationDTO);
		Role role = roleRepo.findByName("ROLE_REGULAR");

		return userRepo.save(mapper.toEntity(userRegistrationDTO, passwordEncoder, role));
	}

	public UserUpdateDTO update(String nickname, String firstName, String lastName) {
		User user = findByNickname(nickname);
		if (!StringUtils.isEmpty(firstName)) {
			user.setFirstName(firstName);
		}
		if (!StringUtils.isEmpty(lastName)) {
			user.setLastName(lastName);
		}
		return mapper.toDTO(userRepo.save(user));
	}

	public void delete(Long id) {
		User user = findById(id);
		user.setActive(false);
		user.setStories(new ArrayList<>());
		user.setPhotos(new ArrayList<>());
		userRepo.save(user);
	}

	public void registrationDTOValidation(UserRegistrationDTO userRegistrationDto) {
		passwordValidator.arePasswordsMatch(userRegistrationDto);
		checkNickname(userRegistrationDto.getNickname());
		checkEmail(userRegistrationDto.getEmail());
	}

	private boolean checkNickname(String nick) {
		try {
			findAnyActive(nick);
			throw new UserValidationException("Nickname exists");
		} catch (UserNotFoundException ex) {
			return true;
		}
	}

	private boolean checkEmail(String email) {
		try {
			findByEmail(email);

			throw new UserValidationException("User with such email exists");
		} catch (UserNotFoundException ex) {
			String[] splitted = email.split("@");
			if (splitted[1].equals(emailDomain)) {
				return true;
			} else throw new UserValidationException("Email domain does not match company domain " + emailDomain);
		}
	}

}