package com.albina.springproject.services;

import com.albina.springproject.view.OrganizationView;
import org.springframework.validation.annotation.Validated;

@Validated
public interface OrganizationService extends CrudService<OrganizationView, Long>{

}
