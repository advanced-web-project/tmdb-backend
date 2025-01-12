package com.movie.tdmb.controller;

import com.movie.tdmb.dto.DataPageResponse;
import com.movie.tdmb.dto.SearchMovieRequest;
import com.movie.tdmb.security.jwt.JwtUtils;
import com.movie.tdmb.service.MovieService;
import com.movie.tdmb.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling search operations related to movies.
 * It provides an endpoint for searching movies with various filters.
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MovieService movieService;

    /**
     * Handles the search request for movies.
     * This method processes a POST request to filter movies based on the provided criteria.
     *
     * @param request  The search parameters including query, genres, categories, dates, user scores, etc.
     * @param pageable Pagination information to return a paginated list of results.
     * @return A ResponseEntity containing a paginated response of movies that match the search criteria.
     */
    @PostMapping("/movie")
    public ResponseEntity<?> searchMovies(
            @RequestBody SearchMovieRequest request,  // Use @RequestBody to retrieve search parameters from the request body
            Pageable pageable
    ) {
        // Call the search service to find movies based on the search request parameters
        List<String> movieIds = searchService.searchMovies(
                request.getQuery(),                // The search query string
                request.getType(),                 // The type of search (e.g., movieName, actorName, naturalQuery)
                request.getReleaseDateBegin(),     // The start date for filtering by release date
                request.getReleaseDateEnd(),       // The end date for filtering by release date
                request.getGenres(),               // A list of genre IDs to filter by
                request.getCategories(),           // A list of categories to filter by
                request.getTrending(),             // Trending filter (day or week)
                request.getUserScoreBegin(),       // Minimum user score for filtering
                request.getUserScoreEnd(),         // Maximum user score for filtering
                request.getThreshold()             // Threshold value for external retrievers
        );

        // Retrieve detailed movie data based on the filtered movie IDs and paginate the results
        DataPageResponse response = movieService.getMoviesByIds(movieIds, pageable);

        // Return the response as an HTTP OK status
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
