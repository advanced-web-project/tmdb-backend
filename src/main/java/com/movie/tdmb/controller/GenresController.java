package com.movie.tdmb.controller;

import com.movie.tdmb.model.Genres;
import com.movie.tdmb.service.GenresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenresController {
    @Autowired
    private GenresService genresService;

    @GetMapping
    public ResponseEntity<?> getAllGenres() {
        List<Genres> response = genresService.getAllGenres();
        return new ResponseEntity<List<Genres>>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Integer id) {
        Genres response = genresService.getGenreById(id);
        return new ResponseEntity<Genres>(response, HttpStatus.OK);
    }

    @PostMapping("/ids")
    public ResponseEntity<?> getGenresByIds(@RequestBody List<Integer> ids) {
        List<Genres> response = genresService.getGenresByIds(ids);
        return new ResponseEntity<List<Genres>>(response, HttpStatus.OK);
    }
}
