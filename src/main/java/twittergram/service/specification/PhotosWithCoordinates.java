package twittergram.service.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import twittergram.entity.Photo;

public class PhotosWithCoordinates implements Specification<Photo> {

    private Double longitude;
    private Double latitude;
    private Integer radius;

    public PhotosWithCoordinates(Double longitude, Double latitude, Integer radius) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    @Override
    public Predicate toPredicate(Root<Photo> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        if (radius == null && longitude != null && latitude != null) {
            Predicate predLongitude = criteriaBuilder.equal(root.get("longitude"), this.longitude);
            Predicate predLatitude = criteriaBuilder.equal(root.get("latitude"), this.latitude);
            return criteriaBuilder.and(predLatitude, predLongitude);
        } else {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }
    }
}
