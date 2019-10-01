package com.albina.springproject.controllers;

import com.albina.springproject.models.Office;
import com.albina.springproject.models.Person;
import com.albina.springproject.models.catalog.Country;
import com.albina.springproject.models.catalog.DocumentType;
import com.albina.springproject.utils.OfficeUtil;
import com.albina.springproject.utils.PersonUtil;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

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
@AutoConfigureTestEntityManager
@Transactional
public class PersonControllerMockMVCTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void listURL_whenGetWithOfficeIdFilter_thenReturnJSONDataNull() throws Exception {
        // given: new office
        Office office = OfficeUtil.getOffice();
        entityManager.persistAndFlush(office);
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
        Person person = entityManager.find(Person.class, 1L);
        // when: filter by given person's fields
        JSONObject filters = new JSONObject();
        filters.put("officeId", person.getOfficeId());
        filters.put("firstName", person.getFirstName());
        filters.put("lastName", person.getLastName());
        filters.put("middleName", person.getMiddleName());
        filters.put("position", person.getPosition());
        filters.put("docCode", person.getDocument().getNumber());
        filters.put("citizenshipCode", person.getCountryId());

        // then: return list with given person
        mvc.perform(post("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filters.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(person.getId()))
                .andExpect(jsonPath("$.data[0].firstName").value(person.getFirstName()))
                .andExpect(jsonPath("$.data[0].lastName").value(person.getLastName()))
                .andExpect(jsonPath("$.data[0].middleName").value(person.getMiddleName()))
                .andExpect(jsonPath("$.data[0].position").value(person.getPosition()));
    }

    @Test
    public void listURL_whenGetWithoutOfficeIdFilter_thenReturnJSONError() throws Exception {
        // given dummy first name
        JSONObject filters = new JSONObject();
        filters.put("firstName", "Test name");

        // when: filter without officeId parameter (only by first name)
        // then: return error with message about required parameter
        mvc.perform(post("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filters.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Required parameter 'officeId' is missing"));
    }

    @Test
    public void getURL_whenIdExists_thenReturnJSONData() throws Exception {
        // given: stored person
        Person person = entityManager.find(Person.class, 1L);
        // when: get person by person's id
        // then: return given person's data
        mvc.perform(get("/api/user/"+person.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(person.getId()))
                .andExpect(jsonPath("$.data.firstName").value(person.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(person.getLastName()))
                .andExpect(jsonPath("$.data.middleName").value(person.getMiddleName()))
                .andExpect(jsonPath("$.data.position").value(person.getPosition()))
                .andExpect(jsonPath("$.data.phone").value(person.getPhone()))
                .andExpect(jsonPath("$.data.docName").value(person.getDocument().getDocumentType().getName()))
                .andExpect(jsonPath("$.data.docNumber").value(person.getDocument().getNumber()))
                .andExpect(jsonPath("$.data.docDate").value(new SimpleDateFormat("MM-dd-yyyy").format(person.getDocument().getDate())))
                .andExpect(jsonPath("$.data.citizenshipName").value(person.getCountry().getName()))
                .andExpect(jsonPath("$.data.citizenshipCode").value(person.getCountryId().toString()))
                .andExpect(jsonPath("$.data.identified").value(person.getIdentified()));
    }

    @Test
    public void getURL_whenIdNotExists_thenReturnJSONError() throws Exception {
        mvc.perform(get("/api/user/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("User with id = 0 can't be found"));
    }

    @Test
    public void saveURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        // given: new person with fully filled attributes
        Person person = PersonUtil.getPerson();
        person.setOfficeId(1L);
        person.getDocument().setDocumentType(new DocumentType((byte) 2, "Паспорт гражданина РФ"));
        person.setCountry(new Country((short) 643, "Российская Федерация"));

        JSONObject itemJson = PersonUtil.personToJson(person);
        // when: save new person
        // then: return success result
        mvc.perform(post("/api/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        // given: a person with filled only necessary attributes
        Person person = PersonUtil.getPersonWithRequired();
        person.setOfficeId(1L);

        JSONObject itemJson = PersonUtil.personToJson(person);
        // when: save new person
        // then: return success result
        mvc.perform(post("/api/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: a person without necessary attributes
        Person person = PersonUtil.getPersonWithoutRequired();

        JSONObject itemJson = PersonUtil.personToJson(person);
        // when: save the person
        // then: return error with message about missed out attributes
        mvc.perform(post("/api/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(content().string(containsString("first name")))
                .andExpect(content().string(containsString("position")))
                .andExpect(content().string(containsString("office id")));
    }

    @Test
    public void updateURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        // given: stored person to update and new person with new data
        Person personToUpdate = entityManager.find(Person.class, 1L);
        Person personValues = PersonUtil.getPerson();
        personToUpdate.setFirstName(personValues.getFirstName());
        personToUpdate.setLastName(personValues.getLastName());
        personToUpdate.setMiddleName(personValues.getMiddleName());
        personToUpdate.setPosition(personValues.getPosition());
        personToUpdate.setOfficeId(1L);
        personToUpdate.setDocument(personValues.getDocument());
        personToUpdate.getDocument().setDocumentType(new DocumentType((byte) 2, "Паспорт гражданина РФ"));

        JSONObject itemJson = PersonUtil.personToJson(personToUpdate);
        // when: update already stored person
        mvc.perform(post("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
        // then: the person was updated
        Person personUpdated = entityManager.find(Person.class, 1L);

        assertThat(personUpdated.equals(personToUpdate)).isTrue();
    }

    @Test
    public void updateURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: stored person and person without necessary attributes
        Person person = entityManager.find(Person.class, 1L);

        Person personToUpdate = PersonUtil.getPersonWithoutRequired();
        person.setId(person.getId());

        JSONObject itemJson = PersonUtil.personToJson(personToUpdate);
        // when: update already stored person with missed out necessary attributes
        // then: return error with message about missed out attributes
        mvc.perform(post("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(content().string(containsString("first name")))
                .andExpect(content().string(containsString("position")))
                .andExpect(content().string(containsString("office id")));
    }
}