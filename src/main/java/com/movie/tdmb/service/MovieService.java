package com.movie.tdmb.service;

import com.movie.tdmb.dto.*;
import com.movie.tdmb.exception.MovieNotFoundException;
import com.movie.tdmb.model.*;
import com.movie.tdmb.repository.MovieRepository;
import com.movie.tdmb.repository.MovieTrendingDayRepository;
import com.movie.tdmb.repository.MovieTrendingWeekRepository;
import com.movie.tdmb.repository.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieTrendingDayRepository movieTrendingDayRepository;
    private final MovieTrendingWeekRepository movieTrendingWeekRepository;
    private final UserHistoryRepository userHistoryRepository;
    public Movie getMovieById(String id) {
        return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found"));
    }
    public Movie getMovieByTmdbId(Long tmdbId) {
        return movieRepository.findByTmdbId(tmdbId).orElseThrow(() -> new MovieNotFoundException("Movie not found"));
    }
    public DataPageResponse getMovieTrendingMovieBaseType(String type,Pageable pageable) {
        if (type.toLowerCase().equals("week")) {
            Page<MovieTrendingWeek> pages = movieTrendingWeekRepository.findAll(pageable);
            return DataPageResponse.builder()
                    .page(pages.getNumber())
                    .totalResults((int) pages.getTotalElements())
                    .perPage(pages.getSize())
                    .totalPages(pages.getTotalPages())
                    .data(pages.getContent())
                    .build();
        }
        if(type.toLowerCase().equals("day")) {
            Page<MovieTrendingDay> pages = movieTrendingDayRepository.findAll(pageable);
            return DataPageResponse.builder()
                    .page(pages.getNumber())
                    .totalResults((int) pages.getTotalElements())
                    .perPage(pages.getSize())
                    .totalPages(pages.getTotalPages())
                    .data(pages.getContent())
                    .build();
        }
        return null;
    }
    public DataPageResponse getMovies(Pageable pageable) {
        Page<Movie> pages = movieRepository.findAll(pageable);
        return DataPageResponse.builder()
                .page(pages.getNumber())
                .totalResults((int) pages.getTotalElements())
                .perPage(pages.getSize())
                .totalPages(pages.getTotalPages())
                .data(pages.getContent())
                .build();
    }

    public DataPageResponse getMovieCategoriesMovieBaseType(String type, Pageable pageable) {
        Page<Movie> pages = movieRepository.findByCategoriesContainingType(type, pageable);
        return DataPageResponse.builder()
                .page(pages.getNumber())
                .totalResults((int) pages.getTotalElements())
                .perPage(pages.getSize())
                .totalPages(pages.getTotalPages())
                .data(pages.getContent())
                .build();
    }

    public DataPageResponse getMoviesByIds(List<String> ids, Pageable pageable) {
        Page<Movie> pages = movieRepository.findMoviesByIds(ids, pageable);

        return DataPageResponse.builder()
                .page(pages.getNumber())
                .totalResults((int) pages.getTotalElements())
                .perPage(pages.getSize())
                .totalPages(pages.getTotalPages())
                .data(pages.getContent())
                .build();
    }

    public DataPageResponseExpand getLastTrailersByCategories(String type, Pageable pageable) {
        int skip = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        List<TrailerWithMovieInfo> trailers = movieRepository.findLastTrailersByCategory(type, skip, limit);

        long totalTrailers = movieRepository.countByCategoriesContainingAndTrailersNotEmpty(type);

        return DataPageResponseExpand.builder()
                .page(pageable.getPageNumber())
                .totalResults((long) totalTrailers)
                .perPage(limit)
                .totalPages((int) Math.ceil((double) totalTrailers / limit))
                .data(trailers)
                .build();
    }
    public void addMovieToUserHistory(String userId, Long tmdbId) {
        UserHistory userHistory = userHistoryRepository.findByUserId(userId)
                .orElse(UserHistory.builder().userId(userId).movies(new HashMap<Long, Integer>()).build());
        userHistory.getMovies().put(tmdbId, userHistory.getMovies().getOrDefault(tmdbId, 0) + 1);
        userHistoryRepository.save(userHistory);
    }

    public List<Movie> getRecommendationMovies(String userId) {
        Optional<UserHistory> userHistory = userHistoryRepository.findByUserId(userId);
        List<Long> movieIds = userHistory.get().getMovies().entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .map(e -> e.getKey())
                .collect(Collectors.toList());
        List<Movie> movies = movieRepository.findMoviesByTmdbIdIn(movieIds);
        Map<Long, Movie> movieMap = movies.stream().collect(Collectors.toMap(Movie::getTmdbId, movie -> movie));
        return movieIds.stream().map(movieMap::get).collect(Collectors.toList());
    }

    public List<Movie> getSimilar(Long tmdbId) {
        Movie movie = movieRepository.findByTmdbId(tmdbId).orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        StringBuilder query = new StringBuilder();
        for(Genre genre: movie.getGenres())
        {
            query.append(genre.getName()).append(" ");
        }
        for(Keyword keyword: movie.getKeywords())
        {
            query.append(keyword.getName()).append(" ");
        }
        query.append(movie.getOverview());
        float threshold = 0.25F;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String apiKey = System.getenv("GEMINI_API_KEY");
            String url = String.format(
                    "https://awd-llm.azurewebsites.net/retriever/?llm_api_key=apiKey&collection_name=movies&query=%s&threshold=%.2f",
                    URLEncoder.encode(query.toString(), StandardCharsets.UTF_8), threshold
            );
             SimilarResponse response = restTemplate.getForObject(url, SimilarResponse.class);
             List<String> ids = response.getData().getResult();
             List<Movie> movies = movieRepository.findMoviesByIds(ids);
             return movies;
        } catch (Exception e) {
            throw new RuntimeException("Failed to call external API");
        }
    }

}
