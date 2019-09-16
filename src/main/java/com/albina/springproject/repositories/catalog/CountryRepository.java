package com.albina.springproject.repositories.catalog;

import com.albina.springproject.models.catalog.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CountryRepository extends JpaRepository<Country, Short> {
}
