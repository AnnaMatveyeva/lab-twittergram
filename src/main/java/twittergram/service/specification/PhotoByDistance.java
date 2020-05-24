package twittergram.service.specification;

import org.springframework.data.jpa.domain.Specification;
import twittergram.entity.Photo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PhotoByDistance implements Specification<Photo> {

	private Double longitude;
	private Double latitude;
	private Integer radius;

	public PhotoByDistance(Double longitude, Double latitude, Integer radius) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.radius = radius;
	}

	@Override
	public Predicate toPredicate(Root<Photo> root, CriteriaQuery<?> criteriaQuery,
								 CriteriaBuilder criteriaBuilder) {
		if (latitude != null && longitude != null & radius != null) {

			Predicate predBetweenLong = criteriaBuilder
					.between(root.get("longitude"), longitude - radius, latitude + radius);
			Predicate predBetweenLat = criteriaBuilder
					.between(root.get("latitude"), latitude - radius, latitude + radius);
			return criteriaBuilder.and(predBetweenLat, predBetweenLong);
		} else {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
		}
	}
}
