package com.albina.springproject.filter.filters;

import com.albina.springproject.filter.FilterSpecificationBuilder;
import com.albina.springproject.filter.SearchCriteria;
import com.albina.springproject.filter.SearchOperation;
import com.albina.springproject.filter.SpecificationBuilder;
import com.albina.springproject.models.Organization;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.constraints.NotNull;

public class OrganizationFilter implements Filter<Organization> {

    @NotNull(message = "Required parameter 'name' is missing")
    private String name;

    private String inn;

    private Boolean isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public Specification<Organization> getFilter() {

        SpecificationBuilder<Organization> spec = new FilterSpecificationBuilder<>();
        spec.addFilter(new SearchCriteria("name", SearchOperation.EQUALS, name));

        if (null != inn && !inn.equals("")) spec.addFilter(new SearchCriteria("inn", SearchOperation.EQUALS, inn));
        if (null != isActive) spec.addFilter(new SearchCriteria("active", SearchOperation.EQUALS, isActive));

        return spec.build();
    }
}
