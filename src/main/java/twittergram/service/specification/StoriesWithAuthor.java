package twittergram.service.specification;

import org.springframework.data.jpa.domain.Specification;
import twittergram.entity.Story;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class StoriesWithAuthor implements Specification<Story> {

	private Long userId;

	public StoriesWithAuthor(Long userId) {
		this.userId = userId;
	}

	@Override
	public Predicate toPredicate(Root<Story> root, CriteriaQuery<?> criteriaQuery,
								 CriteriaBuilder criteriaBuilder) {
		if (userId == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
		}

		return criteriaBuilder.equal(root.get("userId"), this.userId);
	}
}
