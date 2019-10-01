package com.albina.springproject.controllers;


import com.albina.springproject.models.Office;
import com.albina.springproject.models.Organization;
import com.albina.springproject.utils.OfficeUtil;
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

    @Test
    public void listURL_whenGetWithOrgIdFilter_thenReturnJSONDataNull() throws Exception {
        // given: organization without offices
        Organization organization = OrganizationUtil.getOrganization();
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
    public void listURL_whenGetWithFilledFilter_thenReturnJSONDataArray() throws Exception {
        // given: stored office
        Office office = entityManager.find(Office.class, 1L);

        // when: filter with stored office fields
        JSONObject filters = new JSONObject();
        filters.put("orgId", office.getOrganizations().iterator().next().getId());
        filters.put("name", office.getName());
        filters.put("phone", office.getPhone());
        filters.put("isActive", office.isActive());
        // then: return the office's data
        mvc.perform(post("/api/office/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filters.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(office.getId()))
                .andExpect(jsonPath("$.data[0].name").value(office.getName()))
                .andExpect(jsonPath("$.data[0].isActive").value(office.isActive()));
    }

    @Test
    public void listURL_whenGetWithoutOrgIdFilter_thenReturnJSONError() throws Exception {
        // given: dummy data
        JSONObject filters = new JSONObject();
        filters.put("name", "office 1");

        // when: filter with dummy data without required parameter
        // then: return error with that required parameter
        mvc.perform(post("/api/office/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(filters.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Required parameter 'orgId' is missing"));
    }

    @Test
    public void getURL_whenIdIsExist_thenReturnJSONData() throws Exception {
        // given: stored office
        Office office = entityManager.find(Office.class, 1L);
        // when: get with it's id
        // then: return office's data
        mvc.perform(get("/api/office/"+office.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(office.getId()))
                .andExpect(jsonPath("$.data.name").value(office.getName()))
                .andExpect(jsonPath("$.data.phone").value(office.getPhone()))
                .andExpect(jsonPath("$.data.address").value(office.getAddress()))
                .andExpect(jsonPath("$.data.isActive").value(office.isActive()));
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
        Organization organization = OrganizationUtil.getOrganization();
        entityManager.persistAndFlush(organization);

        Office office = OfficeUtil.getOffice(organization);
        JSONObject itemJson = OfficeUtil.officeToJson(office);
        // when: save them
        // then: get success message
        mvc.perform(post("/api/office/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        // given: stored organization and new office entity with required attributes
        Organization organization = OrganizationUtil.getOrganization();
        entityManager.persistAndFlush(organization);

        Office office = OfficeUtil.getOfficeWithRequired();
        office.addOrganization(organization);
        JSONObject itemJson = OfficeUtil.officeToJson(office);
        // when: save them
        // then: get success message
        mvc.perform(post("/api/office/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONError() throws Exception {
        // given: new office entity without required attributes
        Office office = OfficeUtil.getOfficeWithoutRequired();

        JSONObject itemJson = OfficeUtil.officeToJson(office);
        // when: save them
        // then: get error message about missed attributes information
        mvc.perform(post("/api/office/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
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
        Office newOffice = OfficeUtil.getOffice();
        officeToUpdate.setName(newOffice.getName());
        officeToUpdate.setAddress(newOffice.getAddress());
        officeToUpdate.setPhone(newOffice.getName());
        officeToUpdate.setIsActive(false);

        Organization organization = OrganizationUtil.getOrganization();
        entityManager.persistAndFlush(organization);

        officeToUpdate.removeOrganizations(officeToUpdate.getOrganizations());
        officeToUpdate.addOrganization(organization);

        JSONObject itemJson = OfficeUtil.officeToJson(officeToUpdate);

        // when: update office
        mvc.perform(post("/api/office/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
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
        Office officeToUpdate = OfficeUtil.getOfficeWithRequired();
        officeToUpdate.setId(lastOffice.getId());

        Organization organization = OrganizationUtil.getOrganization();
        entityManager.persistAndFlush(organization);

        officeToUpdate.addOrganization(organization);

        JSONObject itemJson = OfficeUtil.officeToJson(officeToUpdate);
        // when: update office
        mvc.perform(post("/api/office/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
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
        Office officeToUpdate = OfficeUtil.getOfficeWithoutRequired();
        officeToUpdate.setId(lastOffice.getId());
        JSONObject itemJson = OfficeUtil.officeToJson(officeToUpdate);
        // when: update office
        // then: get error message about missed attributes information
        mvc.perform(post("/api/office/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(content().string(containsString("address")))
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("organization id")));
    }
}
