package com.albina.springproject.services;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Validated
public interface CrudService<V> {

    /**
     * Save entity to database
     *
     * @param view
     */
    void add(@Valid V view);

    /**
     * Update entity
     *
     * @param view
     */
    void update(@Valid V view);

    /**
     * Get entity by id
     * @param id
     * @return V
     */
    V get(Long id);

    /**
     * Get filtered list of entities
     *
     * @return {V}
     */
    List<V> getFilteredList(Map<String, Object> filters);

    /**
     * Get full list of entities
     *
     * @return {V}
     */
    List<V> all();
}
