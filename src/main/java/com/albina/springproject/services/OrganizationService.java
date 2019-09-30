package com.albina.springproject.services;

import com.albina.springproject.models.Organization;
import com.albina.springproject.services.contracts.CrudService;
import com.albina.springproject.services.contracts.Filterable;
import com.albina.springproject.view.organization.OrganizationItemView;
import com.albina.springproject.view.organization.OrganizationListView;
import org.springframework.validation.annotation.Validated;

@Validated
public interface OrganizationService extends CrudService<OrganizationItemView, Long>, Filterable<OrganizationListView, Organization> {

}
