package twittergram.service.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import twittergram.entity.Photo;
import twittergram.entity.Tag;

public class PhotosWithTag implements Specification<Photo> {

    private String tag;

    public PhotosWithTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Predicate toPredicate(Root<Photo> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        if (tag == null) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        } else {
            Join<Photo, Tag> join = root.join("tags", JoinType.INNER);
            return criteriaBuilder.equal(join.get("text"), tag);
        }
    }
}
