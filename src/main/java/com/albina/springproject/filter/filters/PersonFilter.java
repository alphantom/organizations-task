package com.albina.springproject.filter.filters;

import com.albina.springproject.filter.FilterSpecificationBuilder;
import com.albina.springproject.filter.SearchCriteria;
import com.albina.springproject.filter.SearchOperation;
import com.albina.springproject.filter.SpecificationBuilder;
import com.albina.springproject.models.Person;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.constraints.NotNull;

public class PersonFilter implements Filter<Person> {

    @NotNull(message = "Required parameter 'officeId' is missing")
    private Long officeId;

    private String firstName;

    private String lastName;

    private String middleName;

    private String position;

    private String docCode;

    private Short citizenshipCode;

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public Short getCitizenshipCode() {
        return citizenshipCode;
    }

    public void setCitizenshipCode(Short citizenshipCode) {
        this.citizenshipCode = citizenshipCode;
    }

    @Override
    public Specification<Person> getFilter() {

        SpecificationBuilder<Person> spec = new FilterSpecificationBuilder<>();
        spec.addFilter(new SearchCriteria("officeId", SearchOperation.EQUALS, officeId));

        if (null != firstName && !firstName.equals("")) spec.addFilter(new SearchCriteria("firstName", SearchOperation.EQUALS, firstName));
        if (null != lastName && !lastName.equals("")) spec.addFilter(new SearchCriteria("lastName", SearchOperation.EQUALS, lastName));
        if (null != middleName && !middleName.equals("")) spec.addFilter(new SearchCriteria("middleName", SearchOperation.EQUALS, middleName));
        if (null != position && !position.equals("")) spec.addFilter(new SearchCriteria("position", SearchOperation.EQUALS, position));
        if (null != citizenshipCode) spec.addFilter(new SearchCriteria("countryId", SearchOperation.EQUALS, citizenshipCode));
        if (null != docCode) spec.addFilter(new SearchCriteria("number", SearchOperation.JOIN_HAS, docCode, "document"));

        return spec.build();

    }
}
