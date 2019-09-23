package com.albina.springproject.services;

import com.albina.springproject.view.OrganizationItemView;
import com.albina.springproject.view.OrganizationListView;
import com.albina.springproject.view.OrganizationView;
import org.springframework.validation.annotation.Validated;

@Validated
public interface OrganizationService extends CrudService<OrganizationListView, OrganizationItemView, Long>{

}
