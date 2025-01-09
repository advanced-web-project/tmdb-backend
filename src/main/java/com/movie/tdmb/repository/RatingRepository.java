package com.movie.tdmb.repository;

import com.movie.tdmb.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends MongoRepository<Rating, String> {
    List<Rating> findByUserId(String userId);
    Optional<Rating> findByMovieIdAndUserId(String movieId, String userId);
}
