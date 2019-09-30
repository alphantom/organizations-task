package com.albina.springproject.services.contracts;

import com.albina.springproject.filter.filters.Filter;

import javax.validation.Valid;
import java.util.List;

public interface Filterable<V, E> extends Listable<V> {

    /**
     * Get filtered list of entities
     *
     * @return {V}
     */
    List<V> getFilteredList(@Valid Filter<E> filter);
}
