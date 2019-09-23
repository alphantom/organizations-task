//package com.albina.springproject.controllers;
//
//import com.albina.springproject.common.DataResponse;
//import com.albina.springproject.common.ResultResponse;
//import com.albina.springproject.services.PersonService;
//import com.albina.springproject.view.PersonView;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.util.Map;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//@RestController
//@RequestMapping(value = "/api/user", produces = APPLICATION_JSON_VALUE)
//public class PersonController {
//    private final PersonService personService;
//
//    @Autowired
//    public PersonController(PersonService service) {
//        personService = service;
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity getById(@PathVariable Long id) {
//        return new ResponseEntity<>(new DataResponse<>(personService.get(id)), HttpStatus.OK);
//    }
//
//    @PostMapping("/list")
//    public ResponseEntity list(@RequestBody Map<String, Object> requestBody) {
//        return new ResponseEntity<>(new DataResponse<>(personService.getFilteredList(requestBody)), HttpStatus.OK);
//    }
//
//    @PostMapping("/save")
//    public ResponseEntity save(@RequestBody PersonView person) {
//
//        personService.add(person);
//        return new ResponseEntity<>(new DataResponse<>(new ResultResponse("success")), HttpStatus.OK);
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity update(@RequestBody PersonView person) {
//
//        personService.add(person);
//        return new ResponseEntity<>(new DataResponse<>(new ResultResponse("success")), HttpStatus.OK);
//    }
//}
