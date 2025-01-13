package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseFavoriteListDTO {
    @JsonProperty("tmdb_id")
    private Long tmdbId;
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
