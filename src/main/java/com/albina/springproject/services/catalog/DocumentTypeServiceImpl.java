package com.albina.springproject.services.catalog;

import com.albina.springproject.repositories.catalog.DocumentRepository;
import com.albina.springproject.view.Catalog.DocumentView;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

    @Autowired
    private DocumentRepository documentRepository;
    private MapperFacade mapperFactory = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @Override
    public List<DocumentView> all() {
        return documentRepository.findAll().stream()
                .map(org -> mapperFactory.map(org, DocumentView.class))
                .collect(Collectors.toList());
    }
}
