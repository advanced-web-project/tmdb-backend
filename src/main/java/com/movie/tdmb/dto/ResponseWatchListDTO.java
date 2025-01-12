package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseWatchListDTO {
    @JsonProperty("movie_id")
    private String movieId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("added_at")
    private String addedAt;
}
