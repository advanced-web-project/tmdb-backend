package com.movie.tdmb.controller;
import com.movie.tdmb.dto.DataPageResponse;
import com.movie.tdmb.dto.DataPageResponseExpand;
import com.movie.tdmb.model.Movie;
import com.movie.tdmb.security.jwt.JwtUtils;
import com.movie.tdmb.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final JwtUtils jwtUtils;
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
     * Get movie by tmdb id
     * @param tmdbId
     * @return
     */
    @GetMapping("tmdb/{tmdbId}")
    public ResponseEntity<?> getMovieByTmdbId(@PathVariable Long tmdbId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String userId = jwtUtils.getIdFromJwtToken(authHeader.substring(7));
            movieService.addMovieToUserHistory(userId, tmdbId);
        }
        Movie movie = movieService.getMovieByTmdbId(tmdbId);
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

    @GetMapping("/lasttrailers/categories/{type}")
    public ResponseEntity<?> getLastTrailersByCategories(@PathVariable String type, Pageable pageable) {
        DataPageResponseExpand dataPageResponse = movieService.getLastTrailersByCategories(type, pageable);
        if(dataPageResponse == null) {
            return new ResponseEntity<>("Invalid type", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(dataPageResponse, HttpStatus.OK);
    }

    @GetMapping("recommendation")
    public ResponseEntity<?> getHistoryByUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String userId = jwtUtils.getIdFromJwtToken(authHeader.substring(7));
            return new ResponseEntity<>(movieService.getRecommendationMovies(userId), HttpStatus.OK);
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("similar/{tmdbId}")
    public ResponseEntity<?> getSimilarMovies(@PathVariable Long tmdbId) {
        return new ResponseEntity<>(movieService.getSimilar(tmdbId), HttpStatus.OK);
    }

}
