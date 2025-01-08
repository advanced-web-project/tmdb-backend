package com.movie.tdmb.service;

import com.movie.tdmb.dto.DataPageResponse;
import com.movie.tdmb.exception.MovieNotFoundException;
import com.movie.tdmb.model.Movie;
import com.movie.tdmb.model.MovieTrendingDay;
import com.movie.tdmb.model.MovieTrendingWeek;
import com.movie.tdmb.repository.MovieRepository;
import com.movie.tdmb.repository.MovieTrendingDayRepository;
import com.movie.tdmb.repository.MovieTrendingWeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieTrendingDayRepository movieTrendingDayRepository;
    private final MovieTrendingWeekRepository movieTrendingWeekRepository;
    public Movie getMovieById(String id) {
        return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found"));
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
}