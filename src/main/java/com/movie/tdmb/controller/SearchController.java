package com.movie.tdmb.controller;

import com.movie.tdmb.dto.DataPageResponse;
import com.movie.tdmb.dto.SearchMovieResponseDTO;
import com.movie.tdmb.security.jwt.JwtUtils;
import com.movie.tdmb.service.MovieService;
import com.movie.tdmb.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MovieService movieService;

    @GetMapping("/movie")
    public ResponseEntity<?> searchMovies(
            @RequestParam("query") String query,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "release_date", required = false) String releaseDate,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "trending", required = false) String trending,
            @RequestParam(value = "userscore", required = false) Integer userScore,
            @RequestParam(value = "threshold", required = false) Float threshold,
            @RequestHeader("Authorization") String token,
            Pageable pageable
    ) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));

        List<String> movieIds = searchService.searchMovies(query, type, releaseDate, genre, trending, userScore, userId, threshold);
        DataPageResponse response = movieService.getMoviesByIds(movieIds, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
