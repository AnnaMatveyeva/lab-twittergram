package twittergram.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

	@Column(name = "first_name", nullable = false, length = 40)
	@Size(min = 4, max = 40, message = "First name should contain more than 4,but less than 40 characters")
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 40)
	@Size(min = 4, max = 40, message = "Last name should contain more than 4,but less than 40 characters")
	private String lastName;

	@Column(name = "nickname", nullable = false, length = 40, unique = true)
	@Size(min = 4, max = 40, message = "Nickname should contain more than 4,but less than 40 characters")
	private String nickname;

	@Column(name = "password", nullable = false, length = 255)
	@Size(min = 4, message = "Password should contain more than 4 characters")
	private String password;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	@Email
	@Column(name = "email", nullable = false, length = 40)
	private String email;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private List<Story> stories = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private List<Photo> photos = new ArrayList<>();

	@Column(name = "is_active")
	private boolean isActive;
}
