package twittergram.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import twittergram.entity.Role;
import twittergram.entity.User;
import twittergram.model.UserRegistrationDTO;
import twittergram.model.UserUpdateDTO;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<User, UserUpdateDTO> {

	public User toEntity(UserRegistrationDTO dto, PasswordEncoder passwordEncoder, Role role) {
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

	@Override
	public UserUpdateDTO toDTO(User entity) {
		UserUpdateDTO dto = new UserUpdateDTO();
		dto.setId(entity.getId());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		return dto;
	}
}
