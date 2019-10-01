package com.albina.springproject.controllers;

import com.albina.springproject.models.Organization;
import com.albina.springproject.utils.OrganizationUtil;
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

import static org.hamcrest.CoreMatchers.containsString;
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
        Organization organization = OrganizationUtil.getOrganization();
        entityManager.persistAndFlush(organization);

        JSONObject filters = new JSONObject();
        filters.put("name", organization.getName() );
        filters.put("inn", organization .getInn());
        filters.put("isActive", organization.getIsActive());

        // when: list by given filter
        // then: list with one organization
        mvc.perform(post("/api/organization/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filters.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(organization.getId()))
                .andExpect(jsonPath("$.data[0].name").value(organization.getName()))
                .andExpect(jsonPath("$.data[0].isActive").value(organization.getIsActive()));
    }

    @Test
    public void listURL_whenGetWithoutNameFilter_thenReturnJSONError() throws Exception {
        // given: filter with empty parameters
        JSONObject filters = new JSONObject();
        filters.put("name", null );
        filters.put("inn", null);
        filters.put("isActive", null);

        // when: list by given filter
        // then: error with message about required parameters
        mvc.perform(post("/api/organization/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filters.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Required parameter 'name' is missing"));
    }

    @Test
    public void getURL_whenIdIsExist_thenReturnJSONData() throws Exception {
        // given: stored organization
        Organization organization = OrganizationUtil.getOrganization();
        entityManager.persistAndFlush(organization);

        // when: get organization by it's id
        // then: return organization data
        mvc.perform(get("/api/organization/"+organization.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(organization.getId()))
                .andExpect(jsonPath("$.data.name").value(organization.getName()))
                .andExpect(jsonPath("$.data.fullName").value(organization.getFullName()))
                .andExpect(jsonPath("$.data.inn").value(organization.getInn()))
                .andExpect(jsonPath("$.data.kpp").value(organization.getKpp()))
                .andExpect(jsonPath("$.data.address").value(organization.getAddress()))
                .andExpect(jsonPath("$.data.phone").value(organization.getPhone()))
                .andExpect(jsonPath("$.data.isActive").value(organization.getIsActive()));
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
        Organization organization = OrganizationUtil.getOrganization();
        JSONObject itemJson = OrganizationUtil.organizationToJson(organization);
        // when: save given organization
        // then: result success
        mvc.perform(post("/api/organization/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        // given: new organization with necessary attributes
        Organization organization = OrganizationUtil.getOrganizationWithRequired();

        JSONObject itemJson = OrganizationUtil.organizationToJson(organization);
        // when: save given organization
        // then: result success
        mvc.perform(post("/api/organization/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: new organization without necessary attributes
        Organization organization = OrganizationUtil.getOrganizationWithoutRequired();

        JSONObject itemJson = OrganizationUtil.organizationToJson(organization);
        // when: save given organization
        // then: get an error message with missed attributes information
        mvc.perform(post("/api/organization/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("inn")))
                .andExpect(content().string(containsString("kpp")))
                .andExpect(content().string(containsString("address")))
                .andExpect(content().string(containsString("fullName")));
    }

    @Test
    public void updateURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        // given: stored organization
        Organization newOrganization = OrganizationUtil.getOrganization();
        entityManager.persistAndFlush(newOrganization);

        Organization organizationToUpdate = OrganizationUtil.getOrganization();
        organizationToUpdate.setId(newOrganization.getId());

        JSONObject itemJson = OrganizationUtil.organizationToJson(organizationToUpdate);
        // when: update organization with new data
        // then: result success
        mvc.perform(post("/api/organization/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void updateURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        // given: stored organization
        Organization organization = OrganizationUtil.getOrganization();
        entityManager.persistAndFlush(organization);

        Organization organizationToUpdate = OrganizationUtil.getOrganizationWithRequired();
        organizationToUpdate.setId(organization.getId());

        JSONObject itemJson = OrganizationUtil.organizationToJson(organizationToUpdate);
        // when: update organization with only necessary data in json
        // then: result success
        mvc.perform(post("/api/organization/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void updateURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: stored organization
        Organization organization = OrganizationUtil.getOrganization();
        entityManager.persistAndFlush(organization);

        Organization organizationToUpdate = OrganizationUtil.getOrganizationWithoutRequired();
        organizationToUpdate.setId(organization.getId());

        JSONObject itemJson = OrganizationUtil.organizationToJson(organizationToUpdate);
        // when: update organization without necessary data in json
        // then: error
        mvc.perform(post("/api/organization/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("inn")))
                .andExpect(content().string(containsString("kpp")))
                .andExpect(content().string(containsString("address")))
                .andExpect(content().string(containsString("fullName")));
    }

}
