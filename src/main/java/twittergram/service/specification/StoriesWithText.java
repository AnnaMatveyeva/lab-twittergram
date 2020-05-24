package twittergram.service.specification;

import org.springframework.data.jpa.domain.Specification;
import twittergram.entity.Story;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class StoriesWithText implements Specification<Story> {

	private String text;

	public StoriesWithText(String tag) {
		this.text = tag;
	}

	@Override
	public Predicate toPredicate(Root<Story> root, CriteriaQuery<?> criteriaQuery,
								 CriteriaBuilder criteriaBuilder) {
		if (text == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
		} else {
			return criteriaBuilder.like(root.get("text"), "%" + text + "%");
		}
	}
}
