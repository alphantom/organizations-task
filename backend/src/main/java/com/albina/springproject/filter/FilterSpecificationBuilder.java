package com.albina.springproject.filter;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.Map;

public class FilterSpecificationBuilder<E> implements SpecificationBuilder<E> {

    private final Map<String, Object> params;

    public FilterSpecificationBuilder() {
        params = new HashMap<>();
    }

    public void addFilter(SearchCriteria criteria) {
        params.put(criteria.getKey(), criteria.getValue());
    }

    public Specification<E> build(){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate[] predicates = params.entrySet().stream()
                    .map(crit -> criteriaBuilder.equal(root.get(crit.getKey()), crit.getValue())).toArray(Predicate[]::new);

            return criteriaBuilder.and(predicates);
        };
    }
}
