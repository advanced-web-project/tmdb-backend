package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestFavoriteListDTO {
    @NotNull(message = "TMDB Id is mandatory")
    @JsonProperty("tmdb_id")
    private Long tmdbId;
}
