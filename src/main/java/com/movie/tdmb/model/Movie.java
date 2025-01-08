package com.movie.tdmb.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "movies")
@Data
public class Movie {
    @Id
    private String id;
    private boolean adult;
    private String backdrop_path;
    private Object belongs_to_collection;
    private int budget;
    private Object genres;
    private String homepage;
    private int movie_id;
    private String imdb_id;
    private List<String> origin_country;
    private String original_language;
    private String original_title;
    private String overview;
    private double popularity;
    private String poster_path;
    private Object production_companies;
    private Object production_countries;
    private String release_date;
    private int revenue;
    private Integer runtime;
    private Object spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double vote_average;
    private int vote_count;

}
