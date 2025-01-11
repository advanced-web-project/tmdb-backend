package com.movie.tdmb.service;

import com.movie.tdmb.dto.DataPageResponse;
import com.movie.tdmb.dto.DataPageResponseExpand;
import com.movie.tdmb.exception.MovieNotFoundException;
import com.movie.tdmb.model.Movie;
import com.movie.tdmb.model.MovieTrendingDay;
import com.movie.tdmb.model.MovieTrendingWeek;
import com.movie.tdmb.model.Trailer;
import com.movie.tdmb.repository.MovieRepository;
import com.movie.tdmb.repository.MovieTrendingDayRepository;
import com.movie.tdmb.repository.MovieTrendingWeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieTrendingDayRepository movieTrendingDayRepository;
    private final MovieTrendingWeekRepository movieTrendingWeekRepository;
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


        List<Trailer> trailers = movieRepository.findLastTrailersByCategory(type, skip, limit);

        long totalTrailers = movieRepository.countByCategoriesContainingAndTrailersNotEmpty(type);

        return DataPageResponseExpand.builder()
                .page(pageable.getPageNumber())
                .totalResults((long) totalTrailers)
                .perPage(limit)
                .totalPages((int) Math.ceil((double) totalTrailers / limit))
                .data(trailers)
                .build();
    }

}
