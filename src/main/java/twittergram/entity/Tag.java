package twittergram.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tags")
@Data
public class Tag extends AbstractEntity {

	@Column(name = "text", nullable = false, length = 255, unique = true)
	private String text;

}
