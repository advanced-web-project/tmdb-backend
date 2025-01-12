package com.movie.tdmb.repository;
import com.movie.tdmb.dto.TrailerWithMovieInfo;
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
            "{ $project: { 'trailer': '$trailers', 'tmdb_id': 1, 'title': 1, 'poster_path': 1 } }",
            "{ $skip: ?1 }",
            "{ $limit: ?2 }"
    })
    List<TrailerWithMovieInfo> findLastTrailersByCategory(String category, int skip, int limit);
    @Query(value = "{ 'categories': { $regex: ?0, $options: 'i' }, 'trailers': { $exists: true, $ne: [] } }", count = true)
    long countByCategoriesContainingAndTrailersNotEmpty(String category);
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Movie> findByTitleContaining(String query);
    Optional<Movie> findByTmdbId(Long tmdbId);
    @Aggregation(pipeline = {
            "{ $match: { '_id': { $in: ?0 } } }",
            "{ $match: { $expr: { $and: [ " +
                    "{ $cond: [ { $ne: [?1, null] }, { $gte: ['$release_date', ?1] }, true ] }, " +
                    "{ $cond: [ { $ne: [?2, null] }, { $lte: ['$release_date', ?2] }, true ] } " +
                    "] } } }",
            "{ $match: { $expr: { $cond: [ { $ne: [?3, []] }, { $allElementsTrue: { $map: { input: ?3, as: 'genre', in: { $in: ['$$genre', '$genres.id'] } } } }, true ] } } }",
            "{ $match: { $expr: { $cond: [ { $ne: [?4, []] }, { $allElementsTrue: { $map: { input: ?4, as: 'category', in: { $in: ['$$category', '$categories'] } } } }, true ] } } }",
            "{ $match: { $expr: { $and: [ " +
                    "{ $cond: [ { $ne: [?5, null] }, { $gte: ['$vote_average', ?5] }, true ] }, " +
                    "{ $cond: [ { $ne: [?6, null] }, { $lte: ['$vote_average', ?6] }, true ] } " +
                    "] } } }",
            "{ $project: { _id: 1 } }"
    })
    List<String> findFilteredMovieIds(List<String> ids, String releaseDateBegin, String releaseDateEnd, List<Integer> genres, List<String> categories, Double userScoreBegin, Double userScoreEnd);
}
