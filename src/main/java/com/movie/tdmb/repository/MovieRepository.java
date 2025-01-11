package com.movie.tdmb.repository;
import com.movie.tdmb.model.Movie;
import com.movie.tdmb.model.Trailer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import javax.swing.text.Document;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findById(String id);
    Optional<Movie> findById(Long id);
    @Query("{ '_id': { $in: ?0 } }")
    Page<Movie> findMoviesByIds(List<String> ids, Pageable pageable);
    @Query("{ '_id': { $in: ?0 } }")
    List<Movie> findMoviesByIds(List<String> ids);
    @Query("{ 'id': { $in: ?0 } }")
    List<Movie> findAllByIds(List<Long> ids);
    Page<Movie> findByIdIn(List<String> ids, Pageable pageable);
    Page<Movie> findAll(Pageable pageable);
    @Query("{ 'categories': { $in: [?0] } }")
    Page<Movie> findByCategoriesContainingType(String type, Pageable pageable);
    @Aggregation(pipeline = {
            "{ $match: { 'categories': { $regex: ?0, $options: 'i' }, 'trailers': { $exists: true, $ne: [] } } }",
            "{ $unwind: '$trailers' }",
            "{ $sort: { 'trailers.published_at': -1 } }",
            "{ $replaceRoot: { newRoot: '$trailers' } }",
            "{ $skip: ?1 }",
            "{ $limit: ?2 }"
    })
    List<Trailer> findLastTrailersByCategory(String category, int skip, int limit);
    @Query(value = "{ 'categories': { $regex: ?0, $options: 'i' }, 'trailers': { $exists: true, $ne: [] } }", count = true)
    long countByCategoriesContainingAndTrailersNotEmpty(String category);
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Movie> findByTitleContaining(String query);

}
