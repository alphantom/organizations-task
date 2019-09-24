package com.albina.springproject.services;

import com.albina.springproject.filter.FilterSpecificationBuilder;
import com.albina.springproject.filter.SearchCriteria;
import com.albina.springproject.filter.SearchOperation;
import com.albina.springproject.filter.SpecificationBuilder;
import com.albina.springproject.models.Office;
import com.albina.springproject.models.Organization;
import com.albina.springproject.repositories.OfficeRepository;
import com.albina.springproject.repositories.OrganizationRepository;
import com.albina.springproject.view.OfficeItemView;
import com.albina.springproject.view.OfficeListView;
import com.albina.springproject.view.OfficeView;
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
public class OfficeServiceImpl implements OfficeService{

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    private MapperFacade mapperFactory = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @Override
    public void add(@Valid OfficeItemView officeView) {
        Office office = mapperFactory.map(officeView, Office.class);
        Optional<Organization> optionalOrganization = organizationRepository.findById(officeView.organizationId);
        if (optionalOrganization.isPresent()) {
            office.addOrganization(optionalOrganization.get());
            officeRepository.save(office);
        } else {
            throw new NoSuchElementException("Organization with id = " + officeView.organizationId + " can't be found");
        }
    }

    @Override
    public void update(@Valid OfficeItemView officeView) {
        Optional<Office> optionalOffice = officeRepository.findById(officeView.id);

        if (optionalOffice.isPresent()) {
            Optional<Organization> optionalOrganization = organizationRepository.findById(officeView.organizationId);
            if (optionalOrganization.isPresent()) {
                Office office = optionalOffice.get();

                office.setName(officeView.name);
                office.setAddress(officeView.address);
                office.setPhone(officeView.phone);
                if (null != officeView.isActive) office.setIsActive(officeView.isActive);
                office.removeOrganizations(office.getOrganizations());
                office.addOrganization(optionalOrganization.get());
                officeRepository.save(office);
            } else {
                throw new NoSuchElementException("Organization with id = " + officeView.organizationId + " can't be found");
            }
        } else {
            throw new NoSuchElementException("Office with id = " + officeView.id + " can't be found");
        }
    }

    @Override
    public OfficeItemView get(Long id) {
        Optional<Office> optionalOffice = officeRepository.findById(id);
        if (optionalOffice.isPresent()) {
            return mapperFactory.map(optionalOffice.get(), OfficeItemView.class);
        } else {
            throw new NoSuchElementException("Office with id = " + id + " can't be found");
        }
    }

    @Override
    public List<OfficeListView> all() {
        List<Office> offices = officeRepository.findAll();

        return offices.stream()
                .map(org -> mapperFactory.map(org, OfficeListView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OfficeListView> getFilteredList(Map<String, Object> filter) {
        if (!filter.containsKey("orgId") || null == filter.get("orgId")) {
            throw new IllegalArgumentException("Required parameter 'orgId' is missing");
        }

        SpecificationBuilder<Office> spec = new FilterSpecificationBuilder<>();
        Optional<Organization> optionalOrganization = organizationRepository.findById(Long.valueOf(filter.get("orgId").toString()));
        if (optionalOrganization.isPresent()) {
            spec.addFilter(new SearchCriteria("organizations", SearchOperation.HAS, optionalOrganization.get()));
            filter.remove("orgId");
        } else {
            throw new NoSuchElementException("Organization with id = " + filter.get("orgId") + " can't be found");
        }
        filter.entrySet().stream().filter(item -> null != item.getValue() && !item.getValue().equals(""))
                .forEach(item -> spec.addFilter(new SearchCriteria(item.getKey(), item.getValue())));

        return officeRepository.findAll(spec.build()).stream()
                .map(org -> mapperFactory.map(org, OfficeListView.class))
                .collect(Collectors.toList());
    }
}
