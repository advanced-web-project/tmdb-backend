package com.movie.tdmb.repository;

import com.movie.tdmb.model.MovieTrendingWeek;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieTrendingWeekRepository extends MongoRepository<MovieTrendingWeek,String> {
}
