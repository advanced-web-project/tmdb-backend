package com.movie.tdmb.repository;
import com.movie.tdmb.model.MovieTrendingDay;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieTrendingDayRepository extends MongoRepository<MovieTrendingDay,String> {

}
