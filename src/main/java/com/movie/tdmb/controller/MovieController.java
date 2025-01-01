package com.movie.tdmb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movies")
public class MovieController {
    @GetMapping("test")
    public ResponseEntity<?> test() {
        return new ResponseEntity<>("Test", HttpStatus.CREATED);
    }
}
