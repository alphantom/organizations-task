package com.albina.springproject.services.catalog;

import com.albina.springproject.models.catalog.Country;
import com.albina.springproject.repositories.catalog.CountryRepository;
import com.albina.springproject.view.Catalog.CountryView;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {
    @Autowired
    private CountryRepository countryRepository;
    private MapperFacade mapperFactory = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @Override
    public List<CountryView> all() {
        return countryRepository.findAll().stream()
                .map(org -> mapperFactory.map(org, CountryView.class))
                .collect(Collectors.toList());
    }
}
