package com.movie.tdmb.dto;

import com.movie.tdmb.model.Trailer;
import lombok.Data;

@Data
public class TrailerWithMovieInfo {
    private Trailer trailer;
    private Long tmdb_id;
    private String title;
    private String poster_path;
}
