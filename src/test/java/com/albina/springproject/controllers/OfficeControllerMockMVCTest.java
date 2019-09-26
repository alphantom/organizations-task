package com.albina.springproject.controllers;


import com.albina.springproject.models.Office;
import com.albina.springproject.models.Organization;
import com.albina.springproject.repositories.OfficeRepository;
import com.albina.springproject.repositories.OrganizationRepository;
import com.albina.springproject.seeders.OfficeSeeder;
import com.albina.springproject.seeders.OrganizationSeeder;
import com.albina.springproject.services.OfficeService;
import com.albina.springproject.view.OfficeItemView;
import com.albina.springproject.view.OfficeListView;
import com.albina.springproject.view.OfficeView;
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
public class OfficeControllerMockMVCTest {

    @Autowired
    private MockMvc mvc;

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
                    Stream.generate(OfficeSeeder::getOffice).limit(3).forEach(item::addOffice);
                    organizationRepository.save(item);
                });
    }

    @After
    public void clearData() {
        officeRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    public void listURL_whenGetWithNameFilter_thenReturnJSONDataNull() throws Exception {
        Organization organization = OrganizationSeeder.getOrganization();
        organizationRepository.save(organization);
        mvc.perform(post("/api/office/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orgId\": \""+ organization.getId() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":[]}"));
    }

    @Test
    public void listURL_whenGetWithOrgIdAndNameFilter_thenReturnJSONDataArray() throws Exception {

        Office office = officeRepository.findTopByOrderByIdDesc();
        OfficeListView ov = mapper.map(office, OfficeListView.class);

        Map<String, Object> filters = new HashMap<>();
        filters.put("orgId", office.getOrganizations().iterator().next().getId());
        filters.put("name", ov.name);
        JSONObject jsonFilter = new JSONObject(filters);

        mvc.perform(post("/api/office/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":["+jsonMapper.writeValueAsString(ov) +"]}"));
    }

    @Test
    public void listURL_whenGetWithoutOrgIdFilter_thenReturnJSONError() throws Exception {

        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "office 1");

        JSONObject jsonFilter = new JSONObject(filters);

        mvc.perform(post("/api/office/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Required parameter 'orgId' is missing"));
    }

    @Test
    public void getURL_whenIdIsExist_thenReturnJSONData() throws Exception {
        Office office = officeRepository.findTopByOrderByIdDesc();
        OfficeItemView ov = mapper.map(office, OfficeItemView.class);

        mvc.perform(get("/api/office/"+office.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":" + jsonMapper.writeValueAsString(ov) + "}"));
    }

    @Test
    public void getURL_whenIdIsNotExist_thenReturnJSONError() throws Exception {

        mvc.perform(get("/api/office/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Office with id = 0 can't be found"));
    }

    @Test
    @Transactional
    public void saveURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {
        Organization organization = OrganizationSeeder.getOrganization();
        organizationRepository.save(organization);

        Office office = OfficeSeeder.getOffice(organization);
        OfficeItemView ov = mapper.map(office, OfficeItemView.class);
        mvc.perform(post("/api/office/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

        Optional<Office> optOffice = officeRepository.findOne(Example.of(office, ExampleMatcher.matching()
                .withIgnorePaths("id", "version")));
        assertThat(optOffice.isPresent()).isTrue();

        Office savedOffice = optOffice.get();
        assertThat(savedOffice.getOrganizations().contains(organization)).isTrue();
    }

    @Test
    @Transactional
    public void saveURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {
        Organization organization = OrganizationSeeder.getOrganization();
        organizationRepository.save(organization);

        Office office = OfficeSeeder.getOfficeWithAllowedNull();
        office.addOrganization(organization);
        OfficeItemView ov = mapper.map(office, OfficeItemView.class);

        mvc.perform(post("/api/office/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

        Optional<Office> optOffice = officeRepository.findOne(Example.of(office, ExampleMatcher.matching()
                .withIgnorePaths("id", "version")));
        assertThat(optOffice.isPresent()).isTrue();

        Office savedOffice = optOffice.get();
        assertThat(savedOffice.getOrganizations().contains(organization)).isTrue();
    }

    @Test
    public void saveURL_whenGetNotFull_thenReturnJSONError() throws Exception {

        Office office = OfficeSeeder.getOfficeWithoutAllowedNull();

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
    @Transactional
    public void updateURL_whenGetFullObject_thenReturnJSONDataSuccess() throws Exception {

        Office officeToUpdate = officeRepository.findTopByOrderByIdDesc();
        Office newOffice = OfficeSeeder.getOffice();
        officeToUpdate.setName(newOffice.getName());
        officeToUpdate.setAddress(newOffice.getAddress());
        officeToUpdate.setPhone(newOffice.getName());
        officeToUpdate.setIsActive(false);

        Organization organization = OrganizationSeeder.getOrganization();
        organizationRepository.save(organization);

        officeToUpdate.removeOrganizations(officeToUpdate.getOrganizations());
        officeToUpdate.addOrganization(organization);

        OfficeItemView ov = mapper.map(officeToUpdate, OfficeItemView.class);

        mvc.perform(post("/api/office/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

        Office officeUpdated = officeRepository.findById((long) officeToUpdate.getId());

        assertThat(officeUpdated.equals(officeToUpdate)).isTrue();

    }

    @Test
    @Transactional
    public void updateURL_whenGetNotFull_thenReturnJSONDataSuccess() throws Exception {

        Office lastOffice = officeRepository.findTopByOrderByIdDesc();
        Office officeToUpdate = OfficeSeeder.getOfficeWithAllowedNull();
        officeToUpdate.setId(lastOffice.getId());

        Organization organization = OrganizationSeeder.getOrganization();
        organizationRepository.save(organization);

        officeToUpdate.addOrganization(organization);

        OfficeItemView ov = mapper.map(officeToUpdate, OfficeItemView.class);

        mvc.perform(post("/api/office/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

        Office officeUpdated = officeRepository.findById((long) officeToUpdate.getId());
        assertThat(officeUpdated.equals(officeToUpdate)).isTrue();
    }

    @Test
    public void updateURL_whenGetNotFull_thenReturnJSONError() throws Exception {

        Office lastOffice = officeRepository.findTopByOrderByIdDesc();
        Office officeToUpdate = OfficeSeeder.getOfficeWithoutAllowedNull();
        officeToUpdate.setId(lastOffice.getId());

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
