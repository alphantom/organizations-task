package com.albina.springproject.services;

import com.albina.springproject.view.OrganizationView;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Validated
public interface OrganizationService {

    /**
     * Save new organization to database
     *
     * @param organizationView
     */
    void add(@Valid OrganizationView organizationView);

    /**
     * Update organization
     *
     * @param organizationView
     */
    void update(@Valid OrganizationView organizationView);

    /**
     * Get organization by id
     * @param id
     * @return oganization
     */
    OrganizationView get(Long id);

    /**
     * Get list of organizations
     *
     * @return {organization}
     */
    List<OrganizationView> filteredOrganizations(Map<String, Object> filter);

    List<OrganizationView> all();


}
