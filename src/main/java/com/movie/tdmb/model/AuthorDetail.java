package com.movie.tdmb.model;

import lombok.Data;

@Data
public class AuthorDetail {
    private String name;
    private String username;
    private String avatar_path;
    private Integer rating;
}
