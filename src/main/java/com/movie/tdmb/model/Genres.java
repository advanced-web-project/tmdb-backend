package com.movie.tdmb.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("movie_genres")
@Data
public class Genres {
    @Id
    private String _id;
    private Integer tmdb_id;
    @Field("id")
    private Integer id;
    private String name;
}
