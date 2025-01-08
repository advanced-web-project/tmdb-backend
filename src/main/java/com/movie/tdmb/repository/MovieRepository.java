package com.movie.tdmb.repository;
import com.movie.tdmb.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findById(String id);
}
