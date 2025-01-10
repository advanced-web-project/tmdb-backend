package com.movie.tdmb.service;

import com.movie.tdmb.dto.SearchMovieResponseDTO;
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

    public List<String> searchMovies(String query, String type, String releaseDate, String genre, String trending, Integer userScore, String userId, Float threshold) {
        // Initialize the list for final movie IDs
        List<String> finalMovieIds;

        // Check if trending parameter is provided
        if (trending != null && !trending.isEmpty()) {
            // Get the list of trending movies based on the trending type ("day" or "week").
            List<String> trendingMovieIds = Collections.emptyList();

            if ("day".equalsIgnoreCase(trending)) {
                trendingMovieIds = movieTrendingDayRepository.findAll()
                        .stream()
                        .map(MovieTrendingDay::get_id)
                        .collect(Collectors.toList());
            } else if ("week".equalsIgnoreCase(trending)) {
                trendingMovieIds = movieTrendingWeekRepository.findAll()
                        .stream()
                        .map(MovieTrendingWeek::get_id)
                        .collect(Collectors.toList());
            }

            // Get the list of movies based on the type of query.
            List<String> typeMovieIds = processQueryByType(query, type, threshold);

            // Combine the results from trending and query types.
            if (!trendingMovieIds.isEmpty() && !typeMovieIds.isEmpty()) {
                // Intersection of trending movies and type-based movies.
                finalMovieIds = trendingMovieIds.stream()
                        .filter(typeMovieIds::contains)
                        .collect(Collectors.toList());
            } else {
                // Use whichever list is not empty (or an empty list if both are empty).
                finalMovieIds = !trendingMovieIds.isEmpty() ? trendingMovieIds : typeMovieIds;
            }
        } else {
            // If no trending parameter, only process based on query type
            finalMovieIds = processQueryByType(query, type, threshold);
        }


        // Apply additional filters (release date, genre, user score).
        return applyFilters(finalMovieIds, releaseDate, genre, userScore, userId);
    }

    private List<String> processQueryByType(String query, String type, Float threshold) {
        if ("naturalQuery".equalsIgnoreCase(type)) {
            // Call external API for natural language query processing.
            return callExternalRetrieverAPI(query, threshold);
        } else if ("movieName".equalsIgnoreCase(type)) {
            // Search movies by name in the database.
            return movieRepository.findByTitleContaining(query)
                    .stream()
                    .map(Movie::get_id)
                    .collect(Collectors.toList());
        } else if ("actorName".equalsIgnoreCase(type)) {
            // Search movies by actor name.
            return personRepository.findByNameContainingIgnoreCase(query)
                    .stream()
                    .filter(person -> person.getMovie_credits() != null && person.getMovie_credits().getCast() != null) // Ensure the person has movie credits and cast details.
                    .flatMap(person -> person.getMovie_credits().getCast().stream()) // Flatten the list of casts from all persons.
                    .map(MovieCast::getId) // Extract the movie ID (Long) from the cast details.
                    .distinct() // Remove duplicate movie IDs.
                    .flatMap(movieId -> movieRepository.findById(movieId).stream()) // Fetch movies by ID (convert Long ID to Movie object).
                    .map(Movie::get_id) // Extract the String `_id` from the Movie object.
                    .distinct() // Remove duplicate `_id`.
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    private List<String> applyFilters(List<String> movieIds, String releaseDate, String genre, Integer userScore, String userId) {
        if (releaseDate != null) {
            movieIds = movieIds.stream()
                    .filter(movieId -> {
                        Movie movie = movieRepository.findById(movieId).orElse(null);
                        return movie != null && movie.getRelease_date().startsWith(releaseDate);
                    })
                    .collect(Collectors.toList());
        }

        if (genre != null) {
            movieIds = movieIds.stream()
                    .filter(movieId -> {
                        Movie movie = movieRepository.findById(movieId).orElse(null);
                        return movie != null && movie.getGenres().toString().contains(genre);
                    })
                    .collect(Collectors.toList());
        }

        if (userScore != null) {
            List<String> filteredMovieIds = ratingRepository.findByUserIdAndScore(userId, userScore)
                    .stream()
                    .map(Rating::getMovieId)
                    .collect(Collectors.toList());
            movieIds = movieIds.stream()
                    .filter(filteredMovieIds::contains)
                    .collect(Collectors.toList());
        }

        return movieIds;
    }

    private List<String> callExternalRetrieverAPI(String query, Float threshold) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = String.format(
                    "https://awd-llm.azurewebsites.net/retriever/?llm_api_key=AIzaSyCFzh50x77Vou-QCy9qgF_pKn8QJucL3KY&collection_name=movies&query=%s&threshold=%.2f",
                    URLEncoder.encode(query, StandardCharsets.UTF_8), threshold
            );

            SearchMovieResponseDTO response = restTemplate.getForObject(url, SearchMovieResponseDTO.class);
            if (response != null && response.getData() != null) {
                return response.getData().getResult();
            }
        } catch (Exception e) {
            System.err.println("Error calling external retriever API: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
