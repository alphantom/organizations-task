package com.albina.springproject.controllers;


import com.albina.springproject.models.Office;
import com.albina.springproject.models.Organization;
import com.albina.springproject.seeders.OfficeSeeder;
import com.albina.springproject.seeders.OrganizationSeeder;
import com.albina.springproject.view.office.OfficeItemView;
import com.albina.springproject.view.office.OfficeListView;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class OfficeControllerMockMVCTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    private MapperFacade mapper = new DefaultMapperFactory.Builder().build().getMapperFacade();

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    public void listURL_whenGetWithOrgIdFilter_thenReturnJSONDataNull() throws Exception {
        // given: organization without offices
        Organization organization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(organization);
        // when: filter by it's id
        // then: offices not found
        mvc.perform(post("/api/office/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orgId\": \""+ organization.getId() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":[]}"));
    }

    @Test
    public void listURL_whenGetWithOrgIdAndNameFilter_thenReturnJSONDataArray() throws Exception {
        // given: stored office
        Office office = entityManager.find(Office.class, 1L);
        OfficeListView ov = mapper.map(office, OfficeListView.class);
        // when: filter with stored office fields
        Map<String, Object> filters = new HashMap<>();
        filters.put("orgId", office.getOrganizations().iterator().next().getId());
        filters.put("name", ov.name);
        JSONObject jsonFilter = new JSONObject(filters);
        // then: return the office's data
        mvc.perform(post("/api/office/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":["+jsonMapper.writeValueAsString(ov) +"]}"));
    }

    @Test
    public void listURL_whenGetWithoutOrgIdFilter_thenReturnJSONError() throws Exception {
        // given: dummy data
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "office 1");

        JSONObject jsonFilter = new JSONObject(filters);
        // when: filter with dummy data without required parameter
        // then: return error with that required parameter
        mvc.perform(post("/api/office/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Required parameter 'orgId' is missing"));
    }

    @Test
    public void getURL_whenIdIsExist_thenReturnJSONData() throws Exception {
        // given: stored office
        Office office = entityManager.find(Office.class, 1L);
        OfficeItemView ov = mapper.map(office, OfficeItemView.class);
        // when: get with it's id
        // then: return office's data
        mvc.perform(get("/api/office/"+office.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":" + jsonMapper.writeValueAsString(ov) + "}"));
    }

    @Test
    public void getURL_whenIdIsNotExist_thenReturnJSONError() throws Exception {
        // given: non existing office id
        // when: get with this id
        // then: return error
        mvc.perform(get("/api/office/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Office with id = 0 can't be found"));
    }

    @Test
    public void saveURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        // given: stored organization and new office entity
        Organization organization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(organization);

        Office office = OfficeSeeder.getOffice(organization);
        OfficeItemView ov = mapper.map(office, OfficeItemView.class);
        // when: save them
        // then: get success message
        mvc.perform(post("/api/office/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        // given: stored organization and new office entity with required attributes
        Organization organization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(organization);

        Office office = OfficeSeeder.getOfficeWithAllowedNull();
        office.addOrganization(organization);
        OfficeItemView ov = mapper.map(office, OfficeItemView.class);
        // when: save them
        // then: get success message
        mvc.perform(post("/api/office/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: new office entity without required attributes
        Office office = OfficeSeeder.getOfficeWithoutAllowedNull();
        // when: save them
        // then: get error message about missed attributes information
        mvc.perform(post("/api/office/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(office)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(content().string(containsString("address")))
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("organization id")));
    }

    @Test
    public void updateURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        // given: the stored office which fields was reset
        Office officeToUpdate = entityManager.find(Office.class, 1L);
        Office newOffice = OfficeSeeder.getOffice();
        officeToUpdate.setName(newOffice.getName());
        officeToUpdate.setAddress(newOffice.getAddress());
        officeToUpdate.setPhone(newOffice.getName());
        officeToUpdate.setIsActive(false);

        Organization organization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(organization);

        officeToUpdate.removeOrganizations(officeToUpdate.getOrganizations());
        officeToUpdate.addOrganization(organization);

        OfficeItemView ov = mapper.map(officeToUpdate, OfficeItemView.class);
        // when: update office
        mvc.perform(post("/api/office/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
        // then: result is success and office is updates
        Office officeUpdated = entityManager.find(Office.class, 1L);

        assertThat(officeUpdated.equals(officeToUpdate)).isTrue();

    }

    @Test
    public void updateURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        // given: the stored office and set it's id to the new office with only required data
        Office lastOffice = entityManager.find(Office.class, 1L);
        Office officeToUpdate = OfficeSeeder.getOfficeWithAllowedNull();
        officeToUpdate.setId(lastOffice.getId());

        Organization organization = OrganizationSeeder.getOrganization();
        entityManager.persistAndFlush(organization);

        officeToUpdate.addOrganization(organization);

        OfficeItemView ov = mapper.map(officeToUpdate, OfficeItemView.class);
        // when: update office
        mvc.perform(post("/api/office/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
        // then: result is success and office is updates
        Office officeUpdated = entityManager.find(Office.class, 1L);
        assertThat(officeUpdated.equals(officeToUpdate)).isTrue();
    }

    @Test
    public void updateURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: the stored office and set it's id to the new office without required data
        Office lastOffice = entityManager.find(Office.class, 1L);
        Office officeToUpdate = OfficeSeeder.getOfficeWithoutAllowedNull();
        officeToUpdate.setId(lastOffice.getId());
        // when: update office
        // then: get error message about missed attributes information
        mvc.perform(post("/api/office/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(officeToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(content().string(containsString("address")))
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("organization id")));
    }
}
