package com.albina.springproject.view.CustomMappers;

import com.albina.springproject.models.Document;
import com.albina.springproject.models.Person;
import com.albina.springproject.view.DocumentView;
import com.albina.springproject.view.PersonItemView;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class PersonItemMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
//        factory.classMap(PersonItemView.class, Person.class)
//                .field("document", "document")
//                .field("citizenshipCode", "countryId")
//                .register();
//
//        factory.classMap(DocumentView.class, Document.class)
//                .register();

    }

}
