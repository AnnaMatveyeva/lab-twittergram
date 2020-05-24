package twittergram.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "invalid_tokens")
@Data
public class InvalidToken extends AbstractEntity {

	@Column(name = "token", nullable = false, unique = true, length = 500)
	private String token;

}
