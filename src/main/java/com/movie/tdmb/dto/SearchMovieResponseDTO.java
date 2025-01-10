package com.movie.tdmb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchMovieResponseDTO {
    private int status;
    private SearchMovieResultDTO data;
}
