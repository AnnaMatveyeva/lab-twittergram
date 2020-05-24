package twittergram.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stories")
@Data
public class Story extends AbstractEntity {

	@Column(name = "text", nullable = false, length = 400)
	private String text;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@ManyToMany
	@JoinTable(
			name = "stories_tags",
			joinColumns = @JoinColumn(name = "story_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "stories_likes",
			joinColumns = @JoinColumn(name = "story_id"),
			inverseJoinColumns = @JoinColumn(name = "like_id"))
	private List<Like> likes = new ArrayList<>();

}
