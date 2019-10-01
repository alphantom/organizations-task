package com.albina.springproject.utils;

import com.albina.springproject.models.Organization;
import com.github.javafaker.Faker;
import net.minidev.json.JSONObject;

public class OrganizationUtil {

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

    public static Organization getOrganizationWithRequired() {
        Faker faker = new Faker();
        Organization organization = new Organization();
        organization.setName(faker.company().name());
        organization.setFullName(faker.company().suffix());
        organization.setInn(faker.bothify("############"));
        organization.setKpp(faker.bothify("#########"));
        organization.setAddress(faker.address().fullAddress());

        return organization;
    }

    public static Organization getOrganizationWithoutRequired() {
        Faker faker = new Faker();
        Organization organization = new Organization();
        organization.setPhone(faker.phoneNumber().cellPhone());
        organization.setIsActive(true);

        return organization;
    }

    public static JSONObject organizationToJson(Organization organization) {
        JSONObject itemJson = new JSONObject();
        itemJson.put("id", organization.getId());
        itemJson.put("name", organization.getName());
        itemJson.put("fullName", organization.getFullName());
        itemJson.put("inn", organization.getInn());
        itemJson.put("kpp", organization.getKpp());
        itemJson.put("address", organization.getAddress());
        itemJson.put("phone", organization.getPhone());
        itemJson.put("isActive", organization.getIsActive());

        return itemJson;
    }
}
