package com.albina.springproject.filter.filters;

import com.albina.springproject.filter.FilterSpecificationBuilder;
import com.albina.springproject.filter.SearchCriteria;
import com.albina.springproject.filter.SearchOperation;
import com.albina.springproject.filter.SpecificationBuilder;
import com.albina.springproject.models.Office;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.constraints.NotNull;

public class OfficeFilter implements Filter<Office>{

    private String name;

    @NotNull(message = "Required parameter 'orgId' is missing")
    private Long orgId;

    private Boolean isActive;

    private String phone;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public Specification<Office> getFilter() {
        SpecificationBuilder<Office> spec = new FilterSpecificationBuilder<>();
        spec.addFilter(new SearchCriteria("organizations", SearchOperation.HAS, orgId));

        if (null != name && !name.equals("")) spec.addFilter(new SearchCriteria("name", SearchOperation.EQUALS, name));
        if (null != phone && !phone.equals("")) spec.addFilter(new SearchCriteria("phone", SearchOperation.EQUALS, phone));
        if (null != isActive) spec.addFilter(new SearchCriteria("active", SearchOperation.EQUALS, isActive));

        return spec.build();
    }
}
