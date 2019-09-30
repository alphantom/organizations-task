package com.albina.springproject.services;

import com.albina.springproject.models.Office;
import com.albina.springproject.services.contracts.CrudService;
import com.albina.springproject.services.contracts.Filterable;
import com.albina.springproject.view.office.OfficeItemView;
import com.albina.springproject.view.office.OfficeListView;

public interface OfficeService extends CrudService<OfficeItemView, Long>, Filterable<OfficeListView, Office> {

}
