package com.albina.springproject.services;

import com.albina.springproject.filter.FilterSpecificationBuilder;
import com.albina.springproject.filter.SearchCriteria;
import com.albina.springproject.filter.SpecificationBuilder;
import com.albina.springproject.models.Organization;
import com.albina.springproject.repositories.OrganizationRepository;
import com.albina.springproject.view.OrganizationView;
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
    public void add(@Valid OrganizationView organizationView) {
        Organization organization = mapperFactory.map(organizationView, Organization.class);
        organizationRepository.save(organization);
    }

    @Override
    public void update(@Valid OrganizationView organizationView) {
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
    public OrganizationView get(Long id) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(id);
        if (optionalOrganization.isPresent()) {
            return mapperFactory.map(optionalOrganization.get(), OrganizationView.class);
        } else {
            throw new NoSuchElementException("Organization with id = " + id + " can't be found");
        }
    }

    @Override
    public List<OrganizationView> all() {
        List<Organization> organizations = organizationRepository.findAll();

        return organizations.stream()
                .map(org -> mapperFactory.map(org, OrganizationView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrganizationView> getFilteredList(Map<String, Object> filter) {
        if (!filter.containsKey("name") || null == filter.get("name") || filter.get("name").equals("")) {
            throw new IllegalArgumentException("Required parameter 'name' is missing");
        }

        SpecificationBuilder<Organization> spec = new FilterSpecificationBuilder<>();
        filter.entrySet().stream().filter((item) -> null != item.getValue() && !item.getValue().equals(""))
                .forEach(item -> spec.addFilter(new SearchCriteria(item.getKey(), item.getValue())));

        return organizationRepository.findAll(spec.build()).stream()
                .map(org -> mapperFactory.map(org, OrganizationView.class))
                .collect(Collectors.toList());
    }
}
