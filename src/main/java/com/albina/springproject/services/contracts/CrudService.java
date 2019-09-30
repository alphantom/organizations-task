package com.albina.springproject.services.contracts;

import com.albina.springproject.filter.filters.Filter;
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
public interface CrudService<V, I> {

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
    V get(I id);

}
