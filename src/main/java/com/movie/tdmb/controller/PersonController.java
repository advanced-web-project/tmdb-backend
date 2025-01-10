package com.movie.tdmb.controller;

import com.movie.tdmb.dto.DataPageResponse;
import com.movie.tdmb.model.Person;
import com.movie.tdmb.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("persons")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping
    public ResponseEntity<?> getPersons(Pageable pageable) {
        DataPageResponse dataPageResponse = personService.getPersons(pageable);
        if(dataPageResponse == null) {
            return new ResponseEntity<>("Invalid type", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable String id) {
        Person person = personService.getPersonById(id);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
}
