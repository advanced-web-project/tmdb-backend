package com.movie.tdmb.service;

import com.movie.tdmb.exception.MovieNotFoundException;
import com.movie.tdmb.model.Movie;
import com.movie.tdmb.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    public Movie getMovieById(String id) {
        return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found"));
    }
}
