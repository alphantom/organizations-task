package com.albina.springproject.repositories.catalog;

import com.albina.springproject.models.catalog.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
