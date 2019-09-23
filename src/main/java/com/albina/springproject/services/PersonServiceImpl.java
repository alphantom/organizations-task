//package com.albina.springproject.services;
//
//import com.albina.springproject.filter.FilterSpecificationBuilder;
//import com.albina.springproject.filter.SearchCriteria;
//import com.albina.springproject.filter.SpecificationBuilder;
//import com.albina.springproject.models.Organization;
//import com.albina.springproject.models.Person;
//import com.albina.springproject.repositories.PersonRepository;
//import com.albina.springproject.view.OrganizationView;
//import com.albina.springproject.view.PersonView;
//import ma.glasnost.orika.MapperFacade;
//import ma.glasnost.orika.impl.DefaultMapperFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.validation.Valid;
//import java.util.List;
//import java.util.Map;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class PersonServiceImpl implements PersonService {
//
//    @Autowired
//    private PersonRepository personRepository;
//    private MapperFacade mapperFactory = new DefaultMapperFactory.Builder().build().getMapperFacade();
//
//    @Override
//    public void add(@Valid PersonView view) {
//        Person organization = mapperFactory.map(view, Person.class);
//        personRepository.save(organization);
//    }
//
//    @Override
//    public void update(@Valid PersonView view) {
//        Optional<Person> optionalPerson = personRepository.findById(view.id);
//
//        if (optionalPerson.isPresent()) {
//            Person person = optionalPerson.get();
//
//            person.setFirstName(view.firstName);
//            person.setPosition(view.position);
//            person.setOfficeId(view.officeId);
//            person.setIdentified(view.identified);
//            if (null != view.secondName) person.setSecondName(view.secondName);
//            if (null != view.middleName) person.setMiddleName(view.middleName);
//            if (null != view.phone) person.setPhone(view.phone);
//            if (null != view.documentId) person.setDocumentId(view.documentId);
////            if (null != view.docDate) person.setDocDate(view.docDate);
////            if (null != view.docNumber) person.setDocNumber(view.docNumber);
//            if (null != view.countryId) person.setCountryId(view.countryId);
//
//            personRepository.save(person);
//        } else {
//            throw new NoSuchElementException("Person with id = " + view.id + " can't be found");
//        }
//    }
//
//    @Override
//    public PersonView get(Long id) {
//        Optional<Person> optionalPerson = personRepository.findById(id);
//        if (optionalPerson.isPresent()) {
//            return mapperFactory.map(optionalPerson.get(), PersonView.class);
//        } else {
//            throw new NoSuchElementException("Person with id = " + id + " can't be found");
//        }
//    }
//
//    @Override
//    public List<PersonView> getFilteredList(Map<String, Object> filters) {
////        if (!filters.containsKey("name") || null == filters.get("name") || filters.get("name").equals("")) {
////            throw new IllegalArgumentException("Required parameter 'name' is missing");
////        } TODO : need to implement right validation
//
//        SpecificationBuilder<Person> spec = new FilterSpecificationBuilder<>();
//        filters.entrySet().stream().filter((item) -> null != item.getValue() && !item.getValue().equals(""))
//                .forEach(item -> spec.addFilter(new SearchCriteria(item.getKey(), item.getValue())));
//
//        return personRepository.findAll(spec.build()).stream()
//                .map(org -> mapperFactory.map(org, PersonView.class))
//                .collect(Collectors.toList());
//
//    }
//
//    @Override
//    public List<PersonView> all() {
//        return personRepository.findAll().stream()
//                .map(org -> mapperFactory.map(org, PersonView.class))
//                .collect(Collectors.toList());
//    }
//}
