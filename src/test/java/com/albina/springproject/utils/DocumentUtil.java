package com.albina.springproject.utils;

import com.albina.springproject.models.Document;
import com.github.javafaker.Faker;

public class DocumentUtil {

    public static Document getDocument() {
        Faker faker = new Faker();
        Document document = new Document();
        document.setTypeId((byte) 2);
        document.setNumber(faker.bothify("### #### #####"));
        document.setDate(faker.date().birthday());

        return document;
    }
}
