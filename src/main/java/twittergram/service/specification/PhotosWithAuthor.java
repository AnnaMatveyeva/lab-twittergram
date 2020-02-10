package twittergram.service.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import twittergram.entity.Photo;

public class PhotosWithAuthor implements Specification<Photo> {

    private Long userId;

    public PhotosWithAuthor(Long userId) {
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root<Photo> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        if (userId == null) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }

        return criteriaBuilder.equal(root.get("userId"), this.userId);
    }
}
