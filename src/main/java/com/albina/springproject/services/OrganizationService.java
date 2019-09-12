package com.albina.springproject.services;

import com.albina.springproject.view.OrganizationView;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Validated
public interface OrganizationService extends CrudService<OrganizationView>{

}
