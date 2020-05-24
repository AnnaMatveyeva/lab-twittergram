package twittergram.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "likes")
@Data
public class Like extends AbstractEntity {

	@Column(name = "user_id", nullable = false, unique = true)
	private Long userId;
}
