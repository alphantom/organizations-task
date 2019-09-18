package com.albina.springproject.controllers;

import com.albina.springproject.models.Organization;
import com.albina.springproject.repositories.OrganizationRepository;
import com.albina.springproject.seeders.OrganizationSeeder;
import com.albina.springproject.services.OrganizationService;
import com.albina.springproject.view.OrganizationView;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.validation.constraints.AssertTrue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrganizationControllerMockMVCTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationRepository organizationRepository;

    private MapperFacade mapper = new DefaultMapperFactory.Builder().build().getMapperFacade();

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Before
    public void generate() {
        Stream.generate(OrganizationSeeder::getOrganization)
                .limit(10)
                .forEach(item -> organizationRepository.save(item));
    }

    @Test
    public void listURL_whenGetWithNameFilter_thenReturnJSONDataNull() throws Exception {
        Map<String, Object> filters = new HashMap<>();

        mvc.perform(post("/api/organization/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"org1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":[]}"));
    }

    @Test
    public void listURL_whenGetWithNameFilter_thenReturnJSONDataArray() throws Exception {

        Organization organization = OrganizationSeeder.getOrganization();
        organizationRepository.save(organization);

        Map<String, Object> filters = new HashMap<>();
        filters.put("name", organization.getName() );
        filters.put("inn", organization .getInn());
        filters.put("isActive", organization.getIsActive());

        JSONObject jsonFilter = new JSONObject(filters);

        OrganizationView ov = mapper.map(organization, OrganizationView.class);

        mvc.perform(post("/api/organization/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":["+jsonMapper.writeValueAsString(ov) +"]}"));
    }

    @Test
    public void listURL_whenGetWithoutNameFilter_thenReturnJSONError() throws Exception {

        Map<String, Object> filters = new HashMap<>();
        filters.put("name", null );
        filters.put("inn", null);
        filters.put("isActive", null);

        JSONObject jsonFilter = new JSONObject(filters);

        mvc.perform(post("/api/organization/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFilter.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Required parameter 'name' is missing"));
    }

    @Test
    public void getURL_whenIdIsExist_thenReturnJSONData() throws Exception {

        Organization organization = OrganizationSeeder.getOrganization();
        organizationRepository.save(organization);

        OrganizationView ov = mapper.map(organization, OrganizationView.class);

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

        Organization organization = OrganizationSeeder.getOrganization();
        OrganizationView ov = mapper.map(organization, OrganizationView.class);

        mvc.perform(post("/api/organization/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ov)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.result").value("success"));

        boolean isSaved = organizationRepository.exists(Example.of(organization, ExampleMatcher.matching()
                .withIgnorePaths("id", "version")));

        assertThat(isSaved).isTrue();
    }
//    TODO: 3. /save
//      3.1. GetFullObject then ReturnJSONDataSuccess
//      3.2. GetWithoutDefault then ReturnJSONDataSuccess
//      3.3. GetWithNull then ReturnJSONWithError

//    TODO: 4. /update
//      4.1. GetFullObject then ReturnJSONDataSuccess
//      4.2. GetWithNull then ReturnJSONWithError


}