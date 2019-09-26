package com.albina.springproject.services.catalog;

import com.albina.springproject.repositories.catalog.DocumentTypeRepository;
import com.albina.springproject.view.Catalog.DocumentTypeView;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    private MapperFacade mapperFactory = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @Override
    public List<DocumentTypeView> all() {
        return documentTypeRepository.findAll().stream()
                .map(org -> mapperFactory.map(org, DocumentTypeView.class))
                .collect(Collectors.toList());
    }
}
