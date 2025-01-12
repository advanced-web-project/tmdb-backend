package com.movie.tdmb.service;

import com.movie.tdmb.dto.SearchMovieResponseDTO;
import com.movie.tdmb.exception.InvalidDataException;
import com.movie.tdmb.model.*;
import com.movie.tdmb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for handling movie search operations.
 * This class provides methods to process search queries, filter movies based on various criteria,
 * and retrieve results from the database or external APIs.
 */
@Service
public class SearchService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieTrendingDayRepository movieTrendingDayRepository;

    @Autowired
    private MovieTrendingWeekRepository movieTrendingWeekRepository;

    @Autowired
    private PersonRepository personRepository;

    /**
     * Search for movies based on multiple criteria, such as query type, genres, categories, release date,
     * user score, and trending status.
     *
     * @param query             The search query string.
     * @param type              The type of search (e.g., "movieName", "actorName", "naturalQuery").
     * @param releaseDateBegin  Start date for filtering movies by release date.
     * @param releaseDateEnd    End date for filtering movies by release date.
     * @param genres            List of genre IDs to filter movies by.
     * @param categories        List of categories to filter movies by.
     * @param trending          Indicates whether the movies are trending ("day" or "week").
     * @param userScoreBegin    Minimum user score for filtering movies.
     * @param userScoreEnd      Maximum user score for filtering movies.
     * @param threshold         Threshold for external retriever API.
     * @return A list of movie IDs that match the search criteria.
     */
    public List<String> searchMovies(String query, String type, String releaseDateBegin, String releaseDateEnd,
                                     List<Integer> genres, List<String> categories, String trending,
                                     Double userScoreBegin, Double userScoreEnd, Float threshold) {
        List<Long> finalMovieIds;

        // Filter movies based on trending type if specified
        if (trending != null && !trending.isEmpty()) {
            List<Long> trendingMovieIds = retrieveTrendingMovieIds(trending);

            // Process query based on type and intersect results with trending movies
            List<Long> typeMovieIds = processQueryByType(query, type, threshold);
            finalMovieIds = trendingMovieIds.stream()
                    .filter(typeMovieIds::contains)
                    .collect(Collectors.toList());
        } else {
            // If no trending filter is applied, process query based on type only
            finalMovieIds = processQueryByType(query, type, threshold);
        }

        // Fetch movies from the database by their IDs
        List<Movie> movies = movieRepository.findAllByIds(finalMovieIds);

        // Convert movie entities to a list of string IDs (_id field)
        List<String> finalIds = movies.stream()
                .map(Movie::get_id)
                .collect(Collectors.toList());

        // Apply additional filters (release date, genres, categories, and user scores)
        return applyFilters(finalIds, releaseDateBegin, releaseDateEnd, genres, categories, userScoreBegin, userScoreEnd);
    }

    /**
     * Process the search query based on the specified type, such as natural query, movie name, or actor name.
     *
     * @param query     The search query string.
     * @param type      The type of the query.
     * @param threshold Threshold for external retriever API.
     * @return A list of movie IDs (as Long values) matching the query criteria.
     */
    private List<Long> processQueryByType(String query, String type, Float threshold) {
        switch (type.toLowerCase()) {
            case "naturalquery":
                return callExternalRetrieverAPI(query, threshold);
            case "moviename":
                return movieRepository.findByTitleContaining(query)
                        .stream()
                        .map(movie -> Long.valueOf(movie.getId()))
                        .collect(Collectors.toList());
            case "actorname":
                return personRepository.findByNameContainingIgnoreCase(query)
                        .stream()
                        .filter(person -> person.getMovie_credits() != null &&
                                person.getMovie_credits().getCast() != null &&
                                !person.getMovie_credits().getCast().isEmpty())
                        .flatMap(person -> person.getMovie_credits().getCast().stream())
                        .map(MovieCast::getId)
                        .distinct()
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Invalid query type");
        }
    }

    /**
     * Retrieve trending movie IDs based on the specified trending type ("day" or "week").
     *
     * @param trending The trending type (day/week).
     * @return A list of trending movie IDs.
     */
    private List<Long> retrieveTrendingMovieIds(String trending) {
        if ("day".equalsIgnoreCase(trending)) {
            return movieTrendingDayRepository.findAll()
                    .stream()
                    .map(MovieTrendingDay::getId)
                    .collect(Collectors.toList());
        } else if ("week".equalsIgnoreCase(trending)) {
            return movieTrendingWeekRepository.findAll()
                    .stream()
                    .map(MovieTrendingWeek::getId)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Apply additional filters to a list of movie IDs, such as release date range, genres, categories, and user scores.
     *
     * @param movieIds         List of movie IDs to filter.
     * @param releaseDateBegin Start date for filtering movies by release date.
     * @param releaseDateEnd   End date for filtering movies by release date.
     * @param genres           List of genres to filter by.
     * @param categories       List of categories to filter by.
     * @param userScoreBegin   Minimum user score for filtering.
     * @param userScoreEnd     Maximum user score for filtering.
     * @return A filtered list of movie IDs.
     */
    private List<String> applyFilters(List<String> movieIds, String releaseDateBegin, String releaseDateEnd,
                                      List<Integer> genres, List<String> categories,
                                      Double userScoreBegin, Double userScoreEnd) {
        // Validate date range
        if (releaseDateBegin != null && releaseDateEnd != null && releaseDateEnd.compareTo(releaseDateBegin) < 0) {
            throw new InvalidDataException("Release date end must be greater than or equal to release date begin.");
        }

        // Validate user score range
        if (userScoreBegin != null && userScoreEnd != null && userScoreEnd < userScoreBegin) {
            throw new InvalidDataException("User score end must be greater than or equal to user score begin.");
        }

        // Apply filters using MongoDB repository aggregation
        return movieRepository.findFilteredMovieIds(
                movieIds,
                releaseDateBegin != null ? releaseDateBegin : "0000-01-01",
                releaseDateEnd != null ? releaseDateEnd : "9999-12-31",
                genres != null ? genres : Collections.emptyList(),
                categories != null ? categories : Collections.emptyList(),
                userScoreBegin != null ? userScoreBegin : 0.0,
                userScoreEnd != null ? userScoreEnd : 10.0
        );
    }

    /**
     * Call an external API to process a natural language query.
     *
     * @param query     The query string.
     * @param threshold Threshold for the API.
     * @return A list of movie IDs (as Long values) retrieved from the API.
     */
    private List<Long> callExternalRetrieverAPI(String query, Float threshold) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = String.format(
                    "https://awd-llm.azurewebsites.net/retriever/?llm_api_key=AIzaSyCFzh50x77Vou-QCy9qgF_pKn8QJucL3KY&collection_name=movies&query=%s&threshold=%.2f",
                    URLEncoder.encode(query, StandardCharsets.UTF_8), threshold
            );

            SearchMovieResponseDTO response = restTemplate.getForObject(url, SearchMovieResponseDTO.class);
            if (response != null && response.getData() != null) {
                List<String> stringIds = response.getData().getResult();
                return movieRepository.findMoviesByIds(stringIds)
                        .stream()
                        .map(movie -> Long.valueOf(movie.getId()))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Error calling external retriever API: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
