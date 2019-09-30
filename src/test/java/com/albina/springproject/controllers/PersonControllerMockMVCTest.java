package com.albina.springproject.controllers;

import com.albina.springproject.models.Office;
import com.albina.springproject.models.Person;
import com.albina.springproject.models.catalog.Country;
import com.albina.springproject.models.catalog.DocumentType;
import com.albina.springproject.repositories.OfficeRepository;
import com.albina.springproject.repositories.OrganizationRepository;
import com.albina.springproject.repositories.PersonRepository;
import com.albina.springproject.seeders.OfficeSeeder;
import com.albina.springproject.seeders.OrganizationSeeder;
import com.albina.springproject.seeders.PersonSeeder;
import com.albina.springproject.view.person.PersonItemView;
import com.albina.springproject.view.person.PersonListView;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerMockMVCTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    private MapperFacade mapper = new DefaultMapperFactory.Builder().build().getMapperFacade();

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Before
    public void generate() {

        Stream.generate(OrganizationSeeder::getOrganization)
                .limit(2)
                .forEach(item -> {
                    Stream.generate(OfficeSeeder::getOffice).limit(2).forEach(office -> {
                        Stream.generate(PersonSeeder::getPerson).limit(3).forEach(person -> {
                            office.addPerson(person);
                            person.setOffice(office);
                        });
                        item.addOffice(office);
                    });
                    organizationRepository.save(item);
                });
    }

    @After
    public void clearData() {
        personRepository.deleteAll();
        officeRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    public void listURL_whenGetWithOfficeIdFilter_thenReturnJSONDataNull() throws Exception {
        // given: new office
        Office office = OfficeSeeder.getOffice();
        officeRepository.save(office);
        // when: filter users by new office
        // then: return empty list
        mvc.perform(post("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"officeId\": \""+office.getId()+"\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":[]}"));
    }

    @Test
    public void listURL_whenGetWithFilledFilter_thenReturnJSONDataArray() throws Exception {
        // given: stored person
        Person person = personRepository.findTopByOrderByIdDesc();
        // when: filter by given person's fields
        Map<String, Object> filters = new HashMap<>();
        filters.put("officeId", person.getOfficeId());
        filters.put("firstName", person.getFirstName());
        filters.put("lastName", person.getLastName());
        filters.put("middleName", person.getMiddleName());
        filters.put("position", person.getPosition());
        filters.put("docCode", person.getDocument().getNumber());
        filters.put("citizenshipCode", person.getCountryId());
        JSONObject jsonFilter = new JSONObject(filters);

        PersonListView view = mapper.map(person, PersonListView.class);
        // then: return list with given person
        mvc.perform(post("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":["+jsonMapper.writeValueAsString(view) +"]}"));
    }

    @Test
    public void listURL_whenGetWithoutOfficeIdFilter_thenReturnJSONError() throws Exception {
        // given dummy first name
        Map<String, Object> filters = new HashMap<>();
        filters.put("firstName", "Test name");

        JSONObject jsonFilter = new JSONObject(filters);
        // when: filter without officeId parameter (only by first name)
        // then: return error with message about required parameter
        mvc.perform(post("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Required parameter 'officeId' is missing"));
    }

    @Test
    public void getURL_whenIdIsExist_thenReturnJSONData() throws Exception {
        // given: stored person
        Person person = personRepository.findTopByOrderByIdDesc();
        PersonItemView ov = mapper.map(person, PersonItemView.class);
        // when: get person by person's id
        // then: return given person's data
        mvc.perform(get("/api/user/"+person.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":" + jsonMapper.writeValueAsString(ov) + "}"));
    }

    @Test
    public void getURL_whenIdIsNotExist_thenReturnJSONError() throws Exception {
        mvc.perform(get("/api/user/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("User with id = 0 can't be found"));
    }

    @Test
    public void saveURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        // given: new person with fully filled attributes
        Person person = PersonSeeder.getPerson();
        person.setOfficeId(officeRepository.findTopByOrderByIdDesc().getId());
        person.getDocument().setDocumentType(new DocumentType((byte) 2, "Паспорт гражданина РФ"));
        person.setCountry(new Country((short) 643, "Российская Федерация"));

        PersonItemView view = mapper.map(person, PersonItemView.class);
        // when: save new person
        mvc.perform(post("/api/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(view)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

        // then: the person was stored to database
        Optional<Person> optionalPerson = personRepository.findOne(Example.of(person, ExampleMatcher.matching()
                .withIgnorePaths("id", "version", "document")));
        assertThat(optionalPerson.isPresent()).isTrue();

        Person personSaved = optionalPerson.get();
        assertThat(personSaved.equals(person)).isTrue();
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        // given: a person with filled only necessary attributes
        Person person = PersonSeeder.getPersonWithAllowedNull();
        person.setOfficeId(officeRepository.findTopByOrderByIdDesc().getId());

        PersonItemView view = mapper.map(person, PersonItemView.class);
        // when: save new person
        mvc.perform(post("/api/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(view)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

        // then: the person was stored to database
        Optional<Person> optionalPerson = personRepository.findOne(Example.of(person, ExampleMatcher.matching()
                .withIgnorePaths("id", "version", "document", "countryId")));
        assertThat(optionalPerson.isPresent()).isTrue();

        Person personSaved = optionalPerson.get();
        assertThat(personSaved.equals(person)).isTrue();
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: a person without necessary attributes
        Person person = PersonSeeder.getPersonWithoutAllowedNull();
        // when: save the person
        // then: return error with message about missed out attributes
        mvc.perform(post("/api/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(content().string(containsString("first name")))
                .andExpect(content().string(containsString("position")))
                .andExpect(content().string(containsString("office id")));
    }

    @Test
    public void updateURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        // given: stored person to update and new person with new data
        Person personToUpdate = personRepository.findTopByOrderByIdDesc();
        Person personValues = PersonSeeder.getPerson();
        personToUpdate.setFirstName(personValues.getFirstName());
        personToUpdate.setLastName(personValues.getLastName());
        personToUpdate.setMiddleName(personValues.getMiddleName());
        personToUpdate.setPosition(personValues.getPosition());
        personToUpdate.setOfficeId(officeRepository.findTopByOrderByIdDesc().getId());
        personToUpdate.setDocument(personValues.getDocument());
        personToUpdate.getDocument().setDocumentType(new DocumentType((byte) 2, "Паспорт гражданина РФ"));

        PersonItemView view = mapper.map(personToUpdate, PersonItemView.class);
        // when: update already stored person
        mvc.perform(post("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(view)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
        // then: the person was updated
        Person personUpdated = personRepository.findById((long) personToUpdate.getId());

        assertThat(personUpdated.equals(personToUpdate)).isTrue();
    }

    @Test
    public void updateURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: stored person and person without necessary attributes
        Person personToUpdate = personRepository.findTopByOrderByIdDesc();

        Person person = PersonSeeder.getPersonWithoutAllowedNull();
        person.setId(personToUpdate.getId());
        // when: update already stored person with missed out necessary attributes
        // then: return error with message about missed out attributes
        mvc.perform(post("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(content().string(containsString("first name")))
                .andExpect(content().string(containsString("position")))
                .andExpect(content().string(containsString("office id")));
    }
}