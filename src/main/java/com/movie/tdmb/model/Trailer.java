package com.movie.tdmb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Trailer {
    @Field("iso_639_1")
    private String iso6391;
    @Field("iso_3166_1")
    private String iso31661;
    private String name;
    private String key;
    private String site;
    private Integer size;
    private String type;
    @Field("official")
    private Boolean official;
    @Field("published_at")
    private String publishedAt;
    @Field("id")
    private String id;
}
