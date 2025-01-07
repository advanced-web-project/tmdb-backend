package com.movie.tdmb.repository;

import com.movie.tdmb.model.WatchList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WatchListRepository extends MongoRepository<WatchList, String> {
    List<WatchList> findByUserId(String userId);
}
