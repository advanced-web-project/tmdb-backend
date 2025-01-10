package com.movie.tdmb.model;

import lombok.Data;

@Data
public class Review {
    private String author;
    private AuthorDetail author_details;
    private String content;
    private String created_at;
    private String updated_at;
    private String url;
}
