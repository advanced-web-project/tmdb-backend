package com.movie.tdmb.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "movies")
@Data
public class Movie {
    @Id
    private String _id;
    @Field("id")
    private Integer id;
    private Boolean adult;
    private String backdrop_path;
    private Object belongs_to_collection;
    private Integer budget;
    private Object genres;
    private String homepage;
    private String imdb_id;
    private Object origin_country;
    private String original_language;
    private String original_title;
    private String overview;
    private Double popularity;
    private String poster_path;
    private Object production_companies;
    private Object production_countries;
    private String release_date;
    private Integer revenue;
    private Integer runtime;
    private Object spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private Boolean video;
    private Double vote_average;
    private Integer vote_count;
    private Credit credits;
    private List<Trailer> trailers;
    private List<Keyword> keywords;
    private List<Review> reviews;
}
