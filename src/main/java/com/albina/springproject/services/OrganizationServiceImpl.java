package com.albina.springproject.services;

import com.albina.springproject.filter.FilterSpecificationBuilder;
import com.albina.springproject.filter.SearchCriteria;
import com.albina.springproject.filter.SpecificationBuilder;
import com.albina.springproject.filter.filters.Filter;
import com.albina.springproject.models.Organization;
import com.albina.springproject.repositories.OrganizationRepository;
import com.albina.springproject.view.organization.OrganizationItemView;
import com.albina.springproject.view.organization.OrganizationListView;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    private MapperFacade mapperFactory = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @Override
    public void add(@Valid OrganizationItemView organizationView) {
        Organization organization = mapperFactory.map(organizationView, Organization.class);
        organizationRepository.save(organization);
    }

    @Override
    public void update(@Valid OrganizationItemView organizationView) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(organizationView.id);

        if (optionalOrganization.isPresent()) {
            Organization organization = optionalOrganization.get();

            organization.setName(organizationView.name);
            organization.setFullName(organizationView.fullName);
            organization.setInn(organizationView.inn);
            organization.setKpp(organizationView.kpp);
            organization.setAddress(organizationView.address);
            organization.setPhone(organizationView.phone);
            if (null != organizationView.isActive) organization.setIsActive(organizationView.isActive);

            organizationRepository.save(organization);
        } else {
            throw new NoSuchElementException("Organization with id = " + organizationView.id + " can't be found");
        }
    }

    @Override
    public OrganizationItemView get(Long id) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(id);
        if (optionalOrganization.isPresent()) {
            return mapperFactory.map(optionalOrganization.get(), OrganizationItemView.class);
        } else {
            throw new NoSuchElementException("Organization with id = " + id + " can't be found");
        }
    }

    @Override
    public List<OrganizationListView> all() {
        List<Organization> organizations = organizationRepository.findAll();

        return organizations.stream()
                .map(org -> mapperFactory.map(org, OrganizationListView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrganizationListView> getFilteredList(@Valid Filter<Organization> filter) {
        return organizationRepository.findAll(filter.getFilter()).stream()
                .map(org -> mapperFactory.map(org, OrganizationListView.class))
                .collect(Collectors.toList());
    }
}
