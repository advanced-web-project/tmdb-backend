package com.movie.tdmb.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class MovieCast {
    private Boolean adult;
    private String backdrop_path;
    private List<Integer> genre_ids;
    @Field("id")
    private Long id;
    private String original_language;
    private String original_title;
    private String overview;
    private Double popularity;
    private String poster_path;
    private String release_date;
    private String title;
    private Boolean video;
    private Double vote_average;
    private Integer vote_count;
    private String character;
    private String credit_id;
    private Integer order;
}
