package com.albina.springproject.controllers.catalog;

import com.albina.springproject.common.DataResponse;
import com.albina.springproject.services.catalog.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/api", produces = APPLICATION_JSON_VALUE)
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/documents")
    public ResponseEntity list() {
        return new ResponseEntity<>(new DataResponse<>(documentService.all()), HttpStatus.OK);
    }
}