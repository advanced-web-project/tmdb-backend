package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestWatchListDTO {
    @NotBlank(message = "Movie Id is mandatory")
    @JsonProperty("movie_id")
    private String movieId;
}
