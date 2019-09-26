package com.albina.springproject.seeders;

import com.albina.springproject.models.Office;
import com.albina.springproject.models.Organization;
import com.github.javafaker.Faker;

public class OfficeSeeder {

    public static Office getOffice() {
        Faker faker = new Faker();
        Office office = new Office();
        office.setName(faker.bothify("Office ###"));
        office.setAddress(faker.address().fullAddress());
        office.setPhone(faker.phoneNumber().cellPhone());
        office.setIsActive(true);

        return office;
    }

    public static Office getOffice(Organization organization) {
        Office office = getOffice();
        office.addOrganization(organization);
        return office;
    }

    public static Office getOfficeWithAllowedNull() {
        Faker faker = new Faker();
        Office office = new Office();
        office.setName(faker.bothify("Office ###"));
        office.setAddress(faker.address().fullAddress());
        office.setIsActive(true);

        return office;
    }

    public static Office getOfficeWithoutAllowedNull() {
        Faker faker = new Faker();
        Office office = new Office();
        office.setIsActive(true);

        return office;
    }
}
