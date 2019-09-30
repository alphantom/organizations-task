package com.albina.springproject.filter.filters;

import org.springframework.data.jpa.domain.Specification;

public interface Filter<E> {


    /**
     * Creates org.springframework.data.jpa.domain.Specification to filter
     * @return
     */
    Specification<E> getFilter();
}
