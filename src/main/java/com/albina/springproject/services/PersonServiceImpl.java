package com.albina.springproject.services;

import com.albina.springproject.filter.filters.Filter;
import com.albina.springproject.models.Document;
import com.albina.springproject.models.Office;
import com.albina.springproject.models.Person;
import com.albina.springproject.models.catalog.DocumentType;
import com.albina.springproject.repositories.DocumentRepository;
import com.albina.springproject.repositories.OfficeRepository;
import com.albina.springproject.repositories.PersonRepository;
import com.albina.springproject.repositories.catalog.CountryRepository;
import com.albina.springproject.repositories.catalog.DocumentTypeRepository;
import com.albina.springproject.view.CustomMappers.PersonItemMapper;
import com.albina.springproject.view.person.PersonItemView;
import com.albina.springproject.view.person.PersonListView;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    private OfficeRepository officeRepository;

    private MapperFacade personItemMapper = new PersonItemMapper();
    private MapperFacade mapperFactory = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @Override
    public void add(@Valid PersonItemView view) {
        Person person = new Person();
        personRepository.save(fillPersonFields(person, view));
    }

    @Override
    public void update(@Valid PersonItemView view) {
        Optional<Person> optionalPerson = personRepository.findById(view.id);

        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            personRepository.save(fillPersonFields(person, view));
        } else {
            throw new NoSuchElementException("User with id = " + view.id + " can't be found");
        }
    }

    private Person fillPersonFields(Person person, PersonItemView view) {
        Optional<Office> office = officeRepository.findById(view.officeId);
        if (office.isPresent()) {
            person.setOffice(office.get());
        } else {
            throw new NoSuchElementException("Office with id = " + view.officeId + " can't be found");
        }
        person.setFirstName(view.firstName);
        person.setPosition(view.position);
        person.setIdentified(view.identified);
        person.setLastName(view.lastName);
        person.setMiddleName(view.middleName);
        person.setPhone(view.phone);
        if (null != view.citizenshipCode) person.setCountryId(view.citizenshipCode);

        if (null == view.docNumber) {
            person.setDocument(null);
        } else {
            Document document = null != person.getDocument() ? person.getDocument() : new Document();
            try {
                DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                formatter.setLenient(false);
                document.setDate(formatter.parse(view.docDate));
            } catch (ParseException e) {
                Logger.getGlobal().warning("Incorrect date: " + view.docDate +
                        ". Date should be formatted to MM-dd-yyyy");
            }
            document.setNumber(view.docNumber);
            Optional<DocumentType> optionalDocumentType = documentTypeRepository.findByName(view.docName);
            if (optionalDocumentType.isPresent()) {
                document.setTypeId(optionalDocumentType.get().getCode());
            } else {
                throw new NoSuchElementException("Document type with name = " + view.docName + " can't be found");
            }
            person.setDocument(document);
            document.setPerson(person);
        }
        return person;
    }

    @Override
    public PersonItemView get(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            return mapperFactory.map(optionalPerson.get(), PersonItemView.class);
        } else {
            throw new NoSuchElementException("User with id = " + id + " can't be found");
        }
    }

    @Override
    public List<PersonListView> getFilteredList(@Valid Filter<Person> filter) {
        return personRepository.findAll(filter.getFilter()).stream()
                .map(org -> mapperFactory.map(org, PersonListView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonListView> all() {
        return personRepository.findAll().stream()
                .map(org -> mapperFactory.map(org, PersonListView.class))
                .collect(Collectors.toList());
    }
}
