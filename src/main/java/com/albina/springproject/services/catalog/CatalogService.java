package com.albina.springproject.services.catalog;

import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface CatalogService<V> {

    List<V> all();
}
