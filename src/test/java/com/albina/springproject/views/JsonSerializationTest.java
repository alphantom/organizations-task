package com.albina.springproject.views;

import com.albina.springproject.models.Office;
import com.albina.springproject.models.Organization;
import com.albina.springproject.models.Person;
import com.albina.springproject.models.catalog.Country;
import com.albina.springproject.models.catalog.DocumentType;
import com.albina.springproject.seeders.OfficeSeeder;
import com.albina.springproject.seeders.OrganizationSeeder;
import com.albina.springproject.seeders.PersonSeeder;
import com.albina.springproject.view.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class JsonSerializationTest {

    private ObjectMapper jsonMapper = new ObjectMapper();
    private MapperFacade mapper = new DefaultMapperFactory.Builder().build().getMapperFacade();

    @Test
    public void testOrganizationView() throws Exception {
        Organization organization = OrganizationSeeder.getOrganization();
        OrganizationItemView itemView = mapper.map(organization, OrganizationItemView.class);
        OrganizationListView listView = mapper.map(organization, OrganizationListView.class);

        JSONObject itemJson = new JSONObject();
        itemJson.put("id", organization.getId());
        itemJson.put("name", organization.getName());
        itemJson.put("fullName", organization.getFullName());
        itemJson.put("inn", organization.getInn());
        itemJson.put("kpp", organization.getKpp());
        itemJson.put("address", organization.getAddress());
        itemJson.put("phone", organization.getPhone());
        itemJson.put("isActive", organization.getIsActive());

        JSONObject listJson = new JSONObject();
        listJson.put("id", organization.getId());
        listJson.put("name", organization.getName());
        listJson.put("isActive", organization.getIsActive());

        assertThat(jsonMapper.readValue(itemJson.toString(), Map.class).equals(jsonMapper.readValue(jsonMapper.writeValueAsString(itemView), Map.class))).isTrue();
        assertThat(jsonMapper.readValue(listJson.toString(), Map.class).equals(jsonMapper.readValue(jsonMapper.writeValueAsString(listView), Map.class))).isTrue();
    }

    @Test
    public void testOfficeView() throws Exception {
        Office office = OfficeSeeder.getOffice(OrganizationSeeder.getOrganization());
        OfficeItemView itemView = mapper.map(office, OfficeItemView.class);
        OfficeListView listView = mapper.map(office, OfficeListView.class);

        JSONObject itemJson = new JSONObject();
        itemJson.put("id", office.getId());
        itemJson.put("name", office.getName());
        itemJson.put("address", office.getAddress());
        itemJson.put("phone", office.getPhone());
        itemJson.put("isActive", office.isActive());
        itemJson.put("organizationId", null);

        JSONObject listJson = new JSONObject();
        listJson.put("id", office.getId()); // organization wasn't stored
        listJson.put("name", office.getName());
        listJson.put("phone", office.getPhone());
        listJson.put("isActive", office.isActive());

        System.out.println(itemJson.toString());
        System.out.println(jsonMapper.writeValueAsString(itemView));
        assertThat(jsonMapper.readValue(itemJson.toString(), Map.class).equals(jsonMapper.readValue(jsonMapper.writeValueAsString(itemView), Map.class))).isTrue();
        assertThat(jsonMapper.readValue(listJson.toString(), Map.class).equals(jsonMapper.readValue(jsonMapper.writeValueAsString(listView), Map.class))).isTrue();
    }

    @Test
    public void testPersonView() throws Exception {
        Person person = PersonSeeder.getPerson();
        person.setOfficeId(1L);
        person.getDocument().setDocumentType(new DocumentType((byte) 2, "Паспорт гражданина РФ"));
        person.setCountry(new Country((short) 643, "Российская Федерация"));

        PersonItemView itemView = mapper.map(person, PersonItemView.class);
        PersonListView listView = mapper.map(person, PersonListView.class);

        JSONObject itemJson = new JSONObject();
        itemJson.put("id", person.getId());
        itemJson.put("officeId", person.getOfficeId());
        itemJson.put("firstName", person.getFirstName());
        itemJson.put("lastName", person.getLastName());
        itemJson.put("middleName", person.getMiddleName());
        itemJson.put("position", person.getPosition());
        itemJson.put("phone", person.getPhone());
        itemJson.put("docNumber", person.getDocument().getNumber());
        itemJson.put("docName", person.getDocument().getDocumentType().getName());
        itemJson.put("docDate", new SimpleDateFormat("MM-dd-yyyy").format(person.getDocument().getDate()));
        itemJson.put("citizenshipCode", person.getCountryId());
        itemJson.put("citizenshipName", person.getCountry().getName());
        itemJson.put("identified", person.getIdentified());

        JSONObject listJson = new JSONObject();
        listJson.put("id", person.getId());
        listJson.put("firstName", person.getFirstName());
        listJson.put("lastName", person.getLastName());
        listJson.put("middleName", person.getMiddleName());
        listJson.put("position", person.getPosition());

        assertThat(jsonMapper.readValue(itemJson.toString(), Map.class).equals(jsonMapper.readValue(jsonMapper.writeValueAsString(itemView), Map.class))).isTrue();
        assertThat(jsonMapper.readValue(listJson.toString(), Map.class).equals(jsonMapper.readValue(jsonMapper.writeValueAsString(listView), Map.class))).isTrue();
    }
}
