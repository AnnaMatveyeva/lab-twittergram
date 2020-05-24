package twittergram.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity {

	@Column(name = "name", nullable = false, length = 255, unique = true)
	private String name;
}
