package com.albina.springproject.utils;

import com.albina.springproject.models.Office;
import com.albina.springproject.models.Organization;
import com.github.javafaker.Faker;
import net.minidev.json.JSONObject;

public class OfficeUtil {

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

    public static Office getOfficeWithRequired() {
        Faker faker = new Faker();
        Office office = new Office();
        office.setName(faker.bothify("Office ###"));
        office.setAddress(faker.address().fullAddress());
        office.setIsActive(true);

        return office;
    }

    public static Office getOfficeWithoutRequired() {
        Faker faker = new Faker();
        Office office = new Office();
        office.setIsActive(true);

        return office;
    }

    public static JSONObject officeToJson(Office office) {
        JSONObject itemJson = new JSONObject();
        itemJson.put("id", office.getId());
        itemJson.put("name", office.getName());
        itemJson.put("address", office.getAddress());
        itemJson.put("phone", office.getPhone());
        itemJson.put("isActive", office.isActive());
        if (office.getOrganizations().size() > 0) {
            itemJson.put("organizationId", office.getOrganizations().iterator().next().getId());
        }
        return itemJson;
    }
}
