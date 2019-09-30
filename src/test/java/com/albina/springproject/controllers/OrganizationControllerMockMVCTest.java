package com.albina.springproject.controllers;

import com.albina.springproject.models.Organization;
import com.albina.springproject.seeders.OrganizationSeeder;
import com.albina.springproject.view.organization.OrganizationItemView;
import com.albina.springproject.view.organization.OrganizationListView;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.Before;
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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class OrganizationControllerMockMVCTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    private MapperFacade mapper = new DefaultMapperFactory.Builder().build().getMapperFacade();

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    public void listURL_whenGetWithNameFilter_thenReturnJSONDataNull() throws Exception {
        // given: filter with dummy data
        // when: list by given filter
        // then: return empty list
        mvc.perform(post("/api/organization/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"org1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":[]}"));
    }

    @Test
    public void listURL_whenGetWithNameFilter_thenReturnJSONDataArray() throws Exception {
        // given: filter with stored organization's data
        Organization organization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(organization);

        Map<String, Object> filters = new HashMap<>();
        filters.put("name", organization.getName() );
        filters.put("inn", organization .getInn());
        filters.put("isActive", organization.getIsActive());

        JSONObject jsonFilter = new JSONObject(filters);

        OrganizationListView ov = mapper.map(organization, OrganizationListView.class);
        // when: list by given filter
        // then: list with one organization
        mvc.perform(post("/api/organization/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":["+jsonMapper.writeValueAsString(ov) +"]}"));
    }

    @Test
    public void listURL_whenGetWithoutNameFilter_thenReturnJSONError() throws Exception {
        // given: filter with empty parameters
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", null );
        filters.put("inn", null);
        filters.put("isActive", null);

        JSONObject jsonFilter = new JSONObject(filters);
        // when: list by given filter
        // then: error with message about required parameters
        mvc.perform(post("/api/organization/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Required parameter 'name' is missing"));
    }

    @Test
    public void getURL_whenIdIsExist_thenReturnJSONData() throws Exception {
        // given: stored organization
        Organization organization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(organization);

        OrganizationItemView ov = mapper.map(organization, OrganizationItemView.class);
        // when: get organization by it's id
        // then: return organization data
        mvc.perform(get("/api/organization/"+organization.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":" + jsonMapper.writeValueAsString(ov) + "}"));
    }

    @Test
    public void getURL_whenIdIsNotExist_thenReturnJSONError() throws Exception {

        mvc.perform(get("/api/organization/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Organization with id = 0 can't be found"));
    }

    @Test
    public void saveURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        // given: new fully filled organization
        Organization organization = OrganizationSeeder.getOrganization();
        OrganizationItemView ov = mapper.map(organization, OrganizationItemView.class);
        // when: save given organization
        // then: result success
        mvc.perform(post("/api/organization/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        // given: new organization with necessary attributes
        Organization organization = OrganizationSeeder.getOrganizationWithDefault();
        // when: save given organization
        // then: result success
        mvc.perform(post("/api/organization/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(organization)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: new organization without necessary attributes
        Organization organization = OrganizationSeeder.getOrganizationWithoutDefault();
        // when: save given organization
        // then: error, organization wasn't saved
        mvc.perform(post("/api/organization/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(organization)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void updateURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        // given: stored organization
        Organization newOrganization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(newOrganization);

        Organization organizationToUpdate = OrganizationSeeder.getOrganization();
        organizationToUpdate.setId(newOrganization.getId());

        // when: update organization with new data
        // then: result success
        mvc.perform(post("/api/organization/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(organizationToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void updateURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        // given: stored organization
        Organization organization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(organization);

        Organization organizationToUpdate = OrganizationSeeder.getOrganizationWithDefault();
        organizationToUpdate.setId(organization.getId());
        // when: update organization with only necessary data in json
        // then: result success
        mvc.perform(post("/api/organization/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(organizationToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void updateURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: stored organization
        Organization organization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(organization);

        Organization organizationToUpdate = OrganizationSeeder.getOrganizationWithoutDefault();
        organizationToUpdate.setId(organization.getId());
        // when: update organization without necessary data in json
        // then: error
        mvc.perform(post("/api/organization/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(organizationToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists());
    }

}
