package com.movie.tdmb.service;

import com.movie.tdmb.exception.GenreNotFoundException;
import com.movie.tdmb.exception.MovieNotFoundException;
import com.movie.tdmb.model.Genres;
import com.movie.tdmb.repository.GenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenresService {
    @Autowired
    private GenresRepository genresRepository;

    public List<Genres> getAllGenres() {
        return genresRepository.findAll();
    }

    public Genres getGenreById(Integer id) {
        return genresRepository.findById(id).orElseThrow(() -> new GenreNotFoundException("Genre not found"));
    }

    public List<Genres> getGenresByIds(List<Integer> ids) {
        List<Genres> genres = genresRepository.findByIdIn(ids);
        if (genres.isEmpty()) {
            throw new GenreNotFoundException("No genres found for the provided IDs");
        }
        return genres;
    }
}
