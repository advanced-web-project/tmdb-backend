package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestWatchListDTO {
    @NotBlank(message = "Movie Id is mandatory")
    @JsonProperty("tmdb_id")
    private Long tmdbId;
}
