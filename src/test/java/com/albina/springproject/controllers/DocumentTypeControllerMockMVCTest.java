package com.albina.springproject.controllers;

import com.albina.springproject.repositories.catalog.DocumentTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class DocumentTypeControllerMockMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Test
    public void givenDocumentIndexUri_whenMockMvc_thenCountOfDocumentTypesIsCorrect() throws Exception {

        int countOfDocumentTypes = (int) documentTypeRepository.count();

        mockMvc.perform(get("/api/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data",  hasSize(countOfDocumentTypes)));
    }
}
