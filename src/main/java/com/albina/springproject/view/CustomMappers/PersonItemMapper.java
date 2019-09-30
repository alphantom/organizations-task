package com.albina.springproject.view.CustomMappers;

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
