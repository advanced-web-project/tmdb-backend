package com.movie.tdmb.model;

import lombok.Data;

@Data
public class Trailer {
    private String iso_639_1;
    private String iso_3166_1;
    private String name;
    private String key;
    private String site;
    private Integer size;
    private String type;
    private Boolean official;
    private String published_at;
}
