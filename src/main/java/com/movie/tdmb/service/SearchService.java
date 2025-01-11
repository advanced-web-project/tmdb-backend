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

    /**
     * Search movies based on various criteria such as query type, trending status, release date, genre, etc.
     *
     * @param query         The search query string.
     * @param type          The type of search (e.g., "movieName", "actorName", "naturalQuery").
     * @param releaseDate   The release date to filter movies.
     * @param genre         The genre to filter movies.
     * @param trending      Indicates if movies are trending ("day" or "week").
     * @param userScore     User score filter.
     * @param userId        The user ID for personalized recommendations.
     * @param threshold     Threshold for external retriever API.
     * @return A list of movie IDs that match the search criteria.
     */
    public List<String> searchMovies(String query, String type, String releaseDate, String genre, String trending, Integer userScore, String userId, Float threshold) {
        List<Long> finalMovieIds;

        // Check if the "trending" parameter is provided
        if (trending != null && !trending.isEmpty()) {
            List<Long> trendingMovieIds = Collections.emptyList();

            // Retrieve trending movie IDs based on the trending type
            if ("day".equalsIgnoreCase(trending)) {
                trendingMovieIds = movieTrendingDayRepository.findAll()
                        .stream()
                        .map(MovieTrendingDay::getId)
                        .collect(Collectors.toList());
            } else if ("week".equalsIgnoreCase(trending)) {
                trendingMovieIds = movieTrendingWeekRepository.findAll()
                        .stream()
                        .map(MovieTrendingWeek::getId)
                        .collect(Collectors.toList());
            }

            // Process the query based on its type
            List<Long> typeMovieIds = processQueryByType(query, type, threshold);

            // Combine trending movies and query result (intersection)
            if (!trendingMovieIds.isEmpty() && !typeMovieIds.isEmpty()) {
                finalMovieIds = trendingMovieIds.stream()
                        .filter(typeMovieIds::contains)
                        .collect(Collectors.toList());
            } else {
                finalMovieIds = !trendingMovieIds.isEmpty() ? trendingMovieIds : typeMovieIds;
            }
        } else {
            // If no trending parameter, process only by query type
            finalMovieIds = processQueryByType(query, type, threshold);
        }

        // Fetch movies from the database using their IDs
        List<Movie> movies = movieRepository.findAllByIds(finalMovieIds);

        // Convert movies to their respective String IDs (_id field)
        List<String> finalIds = movies.stream()
                .map(Movie::get_id)
                .collect(Collectors.toList());

        // Apply additional filters based on release date, genre, and user score
        return applyFilters(finalIds, releaseDate, genre, userScore, userId);
    }

    /**
     * Process the query based on its type (e.g., "naturalQuery", "movieName", "actorName").
     *
     * @param query     The search query string.
     * @param type      The type of the query.
     * @param threshold Threshold for external retriever API.
     * @return A list of movie IDs as Long values.
     */
    private List<Long> processQueryByType(String query, String type, Float threshold) {
        if ("naturalQuery".equalsIgnoreCase(type)) {
            // Call external API for natural language query processing
            return callExternalRetrieverAPI(query, threshold);
        } else if ("movieName".equalsIgnoreCase(type)) {
            // Search for movies by their title in the database
            return movieRepository.findByTitleContaining(query)
                    .stream()
                    .map(movie -> Long.valueOf(movie.getId()))
                    .collect(Collectors.toList());
        } else if ("actorName".equalsIgnoreCase(type)) {
            // Search for movies by actor name
            return personRepository.findByNameContainingIgnoreCase(query)
                    .stream()
                    .filter(person -> person.getMovie_credits() != null && person.getMovie_credits().getCast() != null)
                    .flatMap(person -> person.getMovie_credits().getCast().stream())
                    .map(MovieCast::getId) // Extract Long movie ID from the cast
                    .distinct()
                    .flatMap(movieId -> movieRepository.findById(movieId).stream())
                    .map(movie -> Long.valueOf(movie.getId()))
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    /**
     * Apply additional filters to the list of movie IDs.
     *
     * @param movieIds    List of movie IDs.
     * @param releaseDate Release date filter.
     * @param genre       Genre filter.
     * @param userScore   User score filter.
     * @param userId      User ID for personalized filtering.
     * @return Filtered list of movie IDs.
     */
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

    /**
     * Call an external API to process a natural language query.
     *
     * @param query     The query string.
     * @param threshold Threshold for the API.
     * @return A list of movie IDs as Long values.
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
                // Step 1: Get String IDs from the API response
                List<String> stringIds = response.getData().getResult();

                // Step 2: Fetch movies from the database
                List<Movie> movies = movieRepository.findMoviesByIds(stringIds);

                // Step 3: Convert movies to Long IDs
                return movies.stream()
                        .map(movie -> Long.valueOf(movie.getId()))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Error calling external retriever API: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
