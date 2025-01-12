package com.movie.tdmb.repository;

import com.movie.tdmb.model.FavoriteList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteListRepository extends MongoRepository<FavoriteList, String> {
    List<FavoriteList> findByUserId(String userId);
    Optional<FavoriteList> findByMovieIdAndUserId(String movieId, String userId);
}
