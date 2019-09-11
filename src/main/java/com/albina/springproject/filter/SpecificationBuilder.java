package com.albina.springproject.filter;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {

    void addFilter(SearchCriteria criteria);
    Specification<T> build();
}
