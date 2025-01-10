package com.movie.tdmb.controller;
import com.movie.tdmb.dto.DataPageResponse;
import com.movie.tdmb.model.Movie;
import com.movie.tdmb.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    /**
     * Get movie by id
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getMovieById(@PathVariable String id) {
        Movie movie = movieService.getMovieById(id);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }
    /**
     * Get trending movies
     * @param type
     * @param pageable
     * @return
     */
    @GetMapping("/trending/{type}")
    public ResponseEntity<?> getTrendingMovies(@PathVariable String type, Pageable pageable) {
        DataPageResponse dataPageResponse = movieService.getMovieTrendingMovieBaseType(type, pageable);
        if(dataPageResponse == null) {
            return new ResponseEntity<>("Invalid type", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    /**
     * Get all movies
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getMovies(Pageable pageable) {
        DataPageResponse dataPageResponse = movieService.getMovies(pageable);
        if(dataPageResponse == null) {
            return new ResponseEntity<>("Invalid type", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    /**
     * Get movies by categories
     * @param type
     * @param pageable
     * @return
     */
    @GetMapping("/categories/{type}")
    public ResponseEntity<?> getCategoriesMovies(@PathVariable String type, Pageable pageable) {
        DataPageResponse dataPageResponse = movieService.getMovieCategoriesMovieBaseType(type, pageable);
        if(dataPageResponse == null) {
            return new ResponseEntity<>("Invalid type", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }
}
