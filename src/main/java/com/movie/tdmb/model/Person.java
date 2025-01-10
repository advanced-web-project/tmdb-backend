package com.movie.tdmb.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "people")
@Data
public class Person {
    @Id
    private String _id;
    @Field("id")
    private Long id;
    private String tmdb_id;
    private Boolean adult;
    private List<String> also_known_as;
    private String biography;
    private String birthday;
    private String deathday;
    private Integer gender;
    private String homepage;
    private String imdb_id;
    private String known_for_department;
    private String name;
    private String place_of_birth;
    private Double popularity;
    private String profile_path;
    private MovieCredit movie_credits;
}
