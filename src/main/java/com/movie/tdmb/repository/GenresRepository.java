package com.movie.tdmb.repository;

import com.movie.tdmb.model.Genres;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenresRepository extends MongoRepository<Genres, String> {
    List<Genres> findAll();
    Optional<Genres> findById(Integer id);
    @Query("{ 'id': { $in: ?0 } }")
    List<Genres> findByIdIn(List<Integer> ids);
}
