package com.movie.tdmb.repository;
import com.movie.tdmb.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findById(String id);
    List<Movie> findByTitle(String title);
    List<Movie> findByIdIn(List<String> ids);
    Page<Movie> findAll(Pageable pageable);
    @Query("{ 'categories': { $in: [?0] } }")
    Page<Movie> findByCategoriesContainingType(String type, Pageable pageable);
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Movie> findByTitleContaining(String query);

}
