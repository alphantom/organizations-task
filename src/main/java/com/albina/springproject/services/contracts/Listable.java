package com.albina.springproject.services.contracts;

import java.util.List;

public interface Listable<V> {

    /**
     * Get full list of entities
     *
     * @return {V}
     */
    List<V> all();
}
