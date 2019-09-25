package com.albina.springproject.view.CustomMappers;

import com.albina.springproject.models.Document;
import com.albina.springproject.models.Person;
import com.albina.springproject.models.catalog.DocumentType;
import com.albina.springproject.view.PersonItemView;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

import java.util.NoSuchElementException;
import java.util.Optional;

public class PersonItemCustomMapper extends CustomMapper<PersonItemView, Person> {

    @Override
    public void mapAtoB(PersonItemView view, Person person, MappingContext context) {


//        if (null == view.document) {
//            person.setDocument(null);
//        } else {
//            Document document = null != person.getDocument() ? person.getDocument() : new Document();
//            document.setDate(view.docDate);
//            document.setNumber(view.docNumber);
//            Optional<DocumentType> optionalDocumentType = documentTypeRepository.findByName(view.docName);
//            if (optionalDocumentType.isPresent()) {
//                document.setDocumentType(optionalDocumentType.get());
//            } else {
//                throw new NoSuchElementException("Document type with name = " + view.docName + " can't be found");
//            }
//            person.setDocument(document);
//        }

    }
}
