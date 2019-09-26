package com.albina.springproject.seeders;

import com.albina.springproject.models.Organization;
import com.albina.springproject.repositories.OrganizationRepository;
import com.albina.springproject.view.OrganizationView;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizationSeeder {

    public static Organization getOrganization() {
        Faker faker = new Faker();
        Organization organization = new Organization();
        organization.setName(faker.company().name());
        organization.setFullName(faker.company().suffix());
        organization.setInn(faker.bothify("############"));
        organization.setKpp(faker.bothify("#########"));
        organization.setAddress(faker.address().fullAddress());
        organization.setPhone(faker.phoneNumber().cellPhone());
        organization.setIsActive(true);

        return organization;
    }

    public static Organization getOrganizationWithDefault() {
        Faker faker = new Faker();
        Organization organization = new Organization();
        organization.setName(faker.company().name());
        organization.setFullName(faker.company().suffix());
        organization.setInn(faker.bothify("############"));
        organization.setKpp(faker.bothify("#########"));
        organization.setAddress(faker.address().fullAddress());

        return organization;
    }

    public static Organization getOrganizationWithoutDefault() {
        Faker faker = new Faker();
        Organization organization = new Organization();
        organization.setName(faker.company().name());
        organization.setKpp(faker.bothify("#########"));
        organization.setAddress(faker.address().fullAddress());
        organization.setIsActive(true);

        return organization;
    }
}
