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
@Table(name = "photos")
@Data
public class Photo extends AbstractEntity {

	@Column(name = "description", length = 255)
	private String description;

	@Column(name = "image", nullable = false)
	private int image;

	@Column(name = "path", nullable = false, length = 255, unique = true)
	private String path;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@ManyToMany
	@JoinTable(
			name = "photos_tags",
			joinColumns = @JoinColumn(name = "photo_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "photos_likes",
			joinColumns = @JoinColumn(name = "photo_id"),
			inverseJoinColumns = @JoinColumn(name = "like_id"))
	private List<Like> likes = new ArrayList<>();

	private double latitude = 0;

	private double longitude = 0;
}
