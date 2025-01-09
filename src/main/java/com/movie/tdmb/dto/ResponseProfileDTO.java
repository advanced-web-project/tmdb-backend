package com.movie.tdmb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProfileDTO {
    private String userId;
    private String username;
    private String email;
    private List<ResponseWatchListDTO> watchlist;
    private List<ResponseFavoriteListDTO> favoriteList;
    private List<ResponseRatingDTO> ratings;
}
