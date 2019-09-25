package com.albina.springproject.seeders;

import com.albina.springproject.models.Document;
import com.albina.springproject.models.Person;
import com.github.javafaker.Faker;

public class PersonSeeder {

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

        Document doc = DocumentSeeder.getDocument();
        person.setDocument(doc);
        doc.setPerson(person);
//        person.getDocument().
        return person;
    }
//
//    public static Office getOffice(Organization organization) {
//        Office office = getOffice();
//        office.addOrganization(organization);
//        return office;
//    }
//
//    public static Office getOfficeWithAllowedNull() {
//        Faker faker = new Faker();
//        Office office = new Office();
//        office.setName(faker.bothify("Office ###"));
//        office.setAddress(faker.address().fullAddress());
//        office.setIsActive(true);
//
//        return office;
//    }
//
//    public static Office getOfficeWithoutAllowedNull() {
//        Faker faker = new Faker();
//        Office office = new Office();
//        office.setIsActive(true);
//
//        return office;
//    }
}
