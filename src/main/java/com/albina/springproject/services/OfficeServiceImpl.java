package com.albina.springproject.services;

import com.albina.springproject.filter.FilterSpecificationBuilder;
import com.albina.springproject.filter.SearchCriteria;
import com.albina.springproject.filter.SpecificationBuilder;
import com.albina.springproject.models.Office;
import com.albina.springproject.repositories.OfficeRepository;
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

    private MapperFacade mapperFactory = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @Override
    public void add(@Valid OfficeView officeView) {
        Office office = mapperFactory.map(officeView, Office.class);
        officeRepository.save(office);
    }

    @Override
    public void update(@Valid OfficeView officeView) {
        Optional<Office> optionalOffice = officeRepository.findById(officeView.id);

        if (optionalOffice.isPresent()) {
            Office office = optionalOffice.get();

            office.setName(officeView.name);
//            office.setOrganizationId(officeView.organizationId);
            office.setAddress(officeView.address);
            if (null != officeView.phone) office.setPhone(officeView.phone);
            if (null != officeView.isActive) office.setIsActive(officeView.isActive);

            officeRepository.save(office);
        } else {
            throw new NoSuchElementException("Office with id = " + officeView.id + " can't be found");
        }
    }

    @Override
    public OfficeView get(Long id) {
        Optional<Office> optionalOffice = officeRepository.findById(id);
        if (optionalOffice.isPresent()) {
            return mapperFactory.map(optionalOffice.get(), OfficeView.class);
        } else {
            throw new NoSuchElementException("Office with id = " + id + " can't be found");
        }
    }

    @Override
    public List<OfficeView> all() {
        List<Office> offices = officeRepository.findAll();

        return offices.stream()
                .map(org -> mapperFactory.map(org, OfficeView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OfficeView> getFilteredList(Map<String, Object> filter) {
        if (!filter.containsKey("orgId") || null == filter.get("orgId")) {
            throw new IllegalArgumentException("Required parameter 'orgId' is missing");
        }

        SpecificationBuilder<Office> spec = new FilterSpecificationBuilder<>();
        filter.entrySet().stream().filter(item -> null != item.getValue() && !item.getValue().equals(""))
                .forEach(item -> spec.addFilter(new SearchCriteria(item.getKey(), item.getValue())));

        return officeRepository.findAll(spec.build()).stream()
                .map(org -> mapperFactory.map(org, OfficeView.class))
                .collect(Collectors.toList());
    }
}
