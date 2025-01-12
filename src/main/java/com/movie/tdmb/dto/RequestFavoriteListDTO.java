package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestFavoriteListDTO {
    @NotBlank(message = "Movie Id is mandatory")
    @JsonProperty("movie_id")
    private String movieId;
}
