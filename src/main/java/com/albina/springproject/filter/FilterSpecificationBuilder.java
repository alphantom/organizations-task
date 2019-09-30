package com.albina.springproject.filter;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.HashSet;
import java.util.Set;

public class FilterSpecificationBuilder<E> implements SpecificationBuilder<E> {

    private final Set<SearchCriteria> params;

    public FilterSpecificationBuilder() {
        params = new HashSet<>();
    }

    public void addFilter(SearchCriteria criteria) {
        params.add(criteria);
    }

    public Specification<E> build(){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate[] predicates = params.stream()
                    .map(crit -> {
                        switch (crit.getOperation()) {
                            case HAS:
                                return criteriaBuilder.isMember(crit.getValue(), root.get(crit.getKey()));
                            case JOIN_HAS:
                                return criteriaBuilder.equal(root.get(crit.getJoin()).get(crit.getKey()), crit.getValue());
                            default:
                                return criteriaBuilder.equal(root.get(crit.getKey()), crit.getValue());
                        }
                    }).toArray(Predicate[]::new);

            return criteriaBuilder.and(predicates);
        };
    }
}
