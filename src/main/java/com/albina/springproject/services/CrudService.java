package com.albina.springproject.services;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 *
 * @param <S> short view
 * @param <F> full view
 * @param <I> id
 */
@Validated
public interface CrudService<S, F, I> {

    /**
     * Save entity to database
     *
     * @param view
     */
    void add(@Valid F view);

    /**
     * Update entity
     *
     * @param view
     */
    void update(@Valid F view);

    /**
     * Get entity by id
     * @param id
     * @return V
     */
    F get(I id);

    /**
     * Get filtered list of entities
     *
     * @return {V}
     */
    List<S> getFilteredList(Map<String, Object> filters);

    /**
     * Get full list of entities
     *
     * @return {V}
     */
    List<S> all();
}
