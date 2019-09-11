package com.albina.springproject.controllers;


import com.albina.springproject.services.OrganizationService;
import com.albina.springproject.view.OrganizationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/organization", produces = APPLICATION_JSON_VALUE)
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService service) {
        organizationService = service;
    }

    @GetMapping("/{id}")
    public OrganizationView getById(@PathVariable Long id) {
        return organizationService.get(id);
    }

    @PostMapping("/list")
    public List<OrganizationView> list(@RequestBody Map<String, Object> requestBody) {
        return organizationService.filteredOrganizations(requestBody);
    }

    @PostMapping("/save")
    public void save(@RequestBody OrganizationView organization) {
        organizationService.add(organization);
    }

    @PostMapping("/update")
    public void update(@RequestBody OrganizationView organization) {
        organizationService.add(organization);
    }

}
