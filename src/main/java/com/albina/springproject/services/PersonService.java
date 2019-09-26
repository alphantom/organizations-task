package com.albina.springproject.services;


import com.albina.springproject.view.PersonItemView;
import com.albina.springproject.view.PersonListView;

public interface PersonService extends CrudService<PersonListView, PersonItemView, Long>{
}
