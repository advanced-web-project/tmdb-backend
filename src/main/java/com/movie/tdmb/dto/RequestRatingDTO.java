package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestRatingDTO {
    @NotBlank(message = "Movie Id is mandatory")
    @JsonProperty("movie_id")
    private String movieId;

    @NotNull(message = "score is mandatory")
    @JsonProperty("score")
    @Size(min = 0, max = 10, message = "Rating must be between 0 and 10")
    private int score;
}
