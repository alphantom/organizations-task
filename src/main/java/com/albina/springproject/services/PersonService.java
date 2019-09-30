package com.albina.springproject.services;


import com.albina.springproject.models.Person;
import com.albina.springproject.services.contracts.CrudService;
import com.albina.springproject.services.contracts.Filterable;
import com.albina.springproject.view.person.PersonItemView;
import com.albina.springproject.view.person.PersonListView;

public interface PersonService extends CrudService<PersonItemView, Long>, Filterable<PersonListView, Person> {
}
