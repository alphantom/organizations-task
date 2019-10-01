package com.albina.springproject.utils;

import com.albina.springproject.models.Document;
import com.albina.springproject.models.Person;
import com.github.javafaker.Faker;
import net.minidev.json.JSONObject;

import java.text.SimpleDateFormat;

public class PersonUtil {

    public static Person getPerson() {
        Faker faker = new Faker();
        Person person = new Person();
        person.setFirstName(faker.name().firstName());
        person.setLastName(faker.name().lastName());
        person.setMiddleName(faker.name().firstName());
        person.setPosition(faker.job().position());
        person.setPhone(faker.phoneNumber().cellPhone());
        person.setIdentified(true);
        person.setCountryId((short) 643);

        Document doc = DocumentUtil.getDocument();
        person.setDocument(doc);
        doc.setPerson(person);
        return person;
    }

    public static Person getPersonWithRequired() {
        Faker faker = new Faker();
        Person person = new Person();
        person.setFirstName(faker.name().firstName());
        person.setPosition(faker.job().position());
        person.setIdentified(true);

        return person;
    }

    public static Person getPersonWithoutRequired() {
        Faker faker = new Faker();
        Person person = new Person();
        person.setLastName(faker.name().lastName());
        person.setMiddleName(faker.name().firstName());
        return person;
    }

    public static JSONObject personToJson(Person person) {
        JSONObject itemJson = new JSONObject();
        itemJson.put("id", person.getId());
        itemJson.put("officeId", person.getOfficeId());
        itemJson.put("firstName", person.getFirstName());
        itemJson.put("lastName", person.getLastName());
        itemJson.put("middleName", person.getMiddleName());
        itemJson.put("position", person.getPosition());
        itemJson.put("phone", person.getPhone());
        if (null != person.getDocument()) {
            itemJson.put("docNumber", person.getDocument().getNumber());
            itemJson.put("docName", person.getDocument().getDocumentType().getName());
            itemJson.put("docDate", new SimpleDateFormat("MM-dd-yyyy").format(person.getDocument().getDate()));
        }
        if (null != person.getCountry()) {
            itemJson.put("citizenshipCode", person.getCountryId());
            itemJson.put("citizenshipName", person.getCountry().getName());
        }
        itemJson.put("identified", person.getIdentified());
        return itemJson;
    }
}
