package com.movie.tdmb.model;

import lombok.Data;

@Data
public class Cast {
    private Boolean adult;
    private Integer gender;
    private Long id;
    private String known_for_department;
    private String name;
    private String original_name;
    private Double popularity;
    private String profile_path;
    private String character;
    private String credit_id;
    private Integer order;
}

