package twittergram.service.specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import twittergram.entity.Story;

public class StoriesWithDate implements Specification<Story> {

    private String date;

    public StoriesWithDate(String date) {
        this.date = date;
    }

    @Override
    public Predicate toPredicate(Root<Story> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        if (date == null) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }

        return criteriaBuilder
            .equal(root.get("date"), LocalDate.parse(this.date, DateTimeFormatter.ISO_DATE));
    }
}
