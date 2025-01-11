package com.movie.tdmb.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "movies_trending_week")
public class MovieTrendingWeek {
    @Id
    private String _id;
    @Field("id")
    private Long id;
    private String title;
    private String release_date;
    @Field("tmdb_id")
    private Long tmdb_id;
    private String poster_path;
    private Double vote_average;
}
