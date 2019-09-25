package com.albina.springproject.controllers;

import com.albina.springproject.models.Office;
import com.albina.springproject.models.Person;
import com.albina.springproject.models.catalog.DocumentType;
import com.albina.springproject.repositories.OfficeRepository;
import com.albina.springproject.repositories.OrganizationRepository;
import com.albina.springproject.repositories.PersonRepository;
import com.albina.springproject.seeders.OfficeSeeder;
import com.albina.springproject.seeders.OrganizationSeeder;
import com.albina.springproject.seeders.PersonSeeder;
import com.albina.springproject.view.PersonItemView;
import com.albina.springproject.view.PersonListView;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
                .limit(3)
                .forEach(item -> {
                    Stream.generate(OfficeSeeder::getOffice).limit(3).forEach(office -> {
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
        Office office = OfficeSeeder.getOffice();
        officeRepository.save(office);
        mvc.perform(post("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"officeId\": \""+office.getId()+"\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":[]}"));
    }

    @Test
    public void listURL_whenGetWithFilledFilter_thenReturnJSONDataArray() throws Exception {
        Person person = personRepository.findById(1L);

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

        mvc.perform(post("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":["+jsonMapper.writeValueAsString(view) +"]}"));
    }

    @Test
    public void listURL_whenGetWithoutOfficeIdFilter_thenReturnJSONError() throws Exception {

        Map<String, Object> filters = new HashMap<>();
        filters.put("firstName", "Test name");

        JSONObject jsonFilter = new JSONObject(filters);

        mvc.perform(post("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Required parameter 'officeId' is missing"));
    }

    @Test
    public void getURL_whenIdIsExist_thenReturnJSONData() throws Exception {

        Person person = personRepository.findById(1L);
        PersonItemView ov = mapper.map(person, PersonItemView.class);

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
    @Transactional
    public void saveURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        Person person = PersonSeeder.getPerson();
        person.setOfficeId(1L);
        person.getDocument().setDocumentType(new DocumentType((byte) 2, "Паспорт гражданина РФ"));

        PersonItemView view = mapper.map(person, PersonItemView.class);
        mvc.perform(post("/api/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(view)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

        Optional<Person> optionalPerson = personRepository.findOne(Example.of(person, ExampleMatcher.matching()
                .withIgnorePaths("id", "version", "document")));
        assertThat(optionalPerson.isPresent()).isTrue();

        Person personSaved = optionalPerson.get();
        assertThat(personSaved.equals(person)).isTrue();
    }
//
//    @Test
//    @Transactional
//    public void saveURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
//        Organization organization = organizationRepository.findById(1L).get();
//        Office office = OfficeSeeder.getOfficeWithAllowedNull();
//        office.addOrganization(organization);
//        OfficeItemView ov = mapper.map(office, OfficeItemView.class);
//
//        mvc.perform(post("/api/office/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonMapper.writeValueAsString(ov)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.result").value("success"));
//
//        Optional<Office> optOffice = officeRepository.findOne(Example.of(office, ExampleMatcher.matching()
//                .withIgnorePaths("id", "version")));
//        assertThat(optOffice.isPresent()).isTrue();
//
//        Office savedOffice = optOffice.get();
//        assertThat(savedOffice.getOrganizations().contains(organization)).isTrue();
//    }
//
//    @Test
//    public void saveURL_whenGetNotFull_thenReturnJSONError() throws Exception {
//
//        Office office = OfficeSeeder.getOfficeWithoutAllowedNull();
//
//        mvc.perform(post("/api/office/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonMapper.writeValueAsString(office)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(content().string(containsString("address")))
//                .andExpect(content().string(containsString("name")))
//                .andExpect(content().string(containsString("organization id")));
//    }
//
//    @Test
//    @Transactional
//    public void updateURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
//
//        Office officeToUpdate = officeRepository.findById(1L);
//        Office newOffice = OfficeSeeder.getOffice();
//        officeToUpdate.setName(newOffice.getName());
//        officeToUpdate.setAddress(newOffice.getAddress());
//        officeToUpdate.setPhone(newOffice.getName());
//        officeToUpdate.setIsActive(false);
//        officeToUpdate.removeOrganizations(officeToUpdate.getOrganizations());
//        officeToUpdate.addOrganization(organizationRepository.findById(3L).get());
//
//        OfficeItemView ov = mapper.map(officeToUpdate, OfficeItemView.class);
//
//        mvc.perform(post("/api/office/update")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonMapper.writeValueAsString(ov)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.result").value("success"));
//
//        Office officeUpdated = officeRepository.findById(1L);
//
//        assertThat(officeUpdated.equals(officeToUpdate)).isTrue();
//
//    }
//
//    @Test
//    @Transactional
//    public void updateURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
//
//        Office officeToUpdate = OfficeSeeder.getOfficeWithAllowedNull();
//        officeToUpdate.setId(1L);
//        officeToUpdate.addOrganization(organizationRepository.findById(3L).get());
//
//        OfficeItemView ov = mapper.map(officeToUpdate, OfficeItemView.class);
//
//        mvc.perform(post("/api/office/update")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonMapper.writeValueAsString(ov)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.result").value("success"));
//
//        Office officeUpdated = officeRepository.findById(1L);
//        assertThat(officeUpdated.equals(officeToUpdate)).isTrue();
//    }
//
//    @Test
//    public void updateURL_whenGetNotFull_thenReturnJSONError() throws Exception {
//
//        Office officeToUpdate = OfficeSeeder.getOfficeWithoutAllowedNull();
//        officeToUpdate.setId(1L);
//
//        mvc.perform(post("/api/office/update")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonMapper.writeValueAsString(officeToUpdate)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(content().string(containsString("address")))
//                .andExpect(content().string(containsString("name")))
//                .andExpect(content().string(containsString("organization id")));
//    }
}