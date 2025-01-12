package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchMovieRequest {
    @NotNull(message = "Query is mandatory")
    private String query;

    @Pattern(regexp = "movieName|actorName|naturalQuery", message = "Type must be one of the following values: movieName, actorName, naturalQuery")
    private String type;

    @JsonProperty("release_date_begin")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Release date must be in the format yyyy-MM-dd")
    private String releaseDateBegin;

    @JsonProperty("release_date_end")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Release date must be in the format yyyy-MM-dd")
    private String releaseDateEnd;

    @Pattern(regexp = "popular|top_rated|now_playing|up_coming", message = "Type must be one of the following values: movieName, actorName, naturalQuery")
    private List<String> categories;

    private List<Integer> genres;

    @Pattern(regexp = "day|week", message = "Trending must be one of the following values: day, week")
    private String trending;

    @JsonProperty("user_score_begin")
    @Min(value = 0, message = "User score begin must be between 0 and 10")
    @Max(value = 10, message = "User score begin must be between 0 and 10")
    private Double userScoreBegin;

    @JsonProperty("user_score_end")
    @Min(value = 0, message = "User score end must be between 0 and 10")
    @Max(value = 10, message = "User score end must be between 0 and 10")
    private Double userScoreEnd;

    private Float threshold;
}
