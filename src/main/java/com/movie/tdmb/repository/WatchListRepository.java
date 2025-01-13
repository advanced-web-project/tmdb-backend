package com.movie.tdmb.repository;

import com.movie.tdmb.model.WatchList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WatchListRepository extends MongoRepository<WatchList, String> {
    List<WatchList> findByUserId(String userId);
    Optional<WatchList> findByTmdbIdAndUserId(Long tmdbId, String userId);
}
