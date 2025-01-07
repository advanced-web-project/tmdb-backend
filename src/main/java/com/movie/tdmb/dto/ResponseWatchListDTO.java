package com.movie.tdmb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Name;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseWatchListDTO {
    @JsonProperty("movie_id")
    private String movieId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("added_at")
    private LocalDateTime addedAt;
}
