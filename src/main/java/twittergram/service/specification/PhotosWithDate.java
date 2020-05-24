package twittergram.service.specification;

import org.springframework.data.jpa.domain.Specification;
import twittergram.entity.Photo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PhotosWithDate implements Specification<Photo> {

	private String date;

	public PhotosWithDate(String date) {
		this.date = date;
	}

	@Override
	public Predicate toPredicate(Root<Photo> root, CriteriaQuery<?> criteriaQuery,
								 CriteriaBuilder criteriaBuilder) {
		if (date == null) {
			return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
		}

		return criteriaBuilder
				.equal(root.get("date"), LocalDate.parse(this.date, DateTimeFormatter.ISO_DATE));
	}
}
