package com.movie.tdmb.service;

import com.movie.tdmb.dto.ResponseFavoriteListDTO;
import com.movie.tdmb.dto.ResponseProfileDTO;
import com.movie.tdmb.dto.ResponseRatingDTO;
import com.movie.tdmb.dto.ResponseWatchListDTO;
import com.movie.tdmb.exception.UserNotFoundException;
import com.movie.tdmb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private FavoriteListService favoriteListService;

    @Autowired
    private RatingService ratingService;

    public ResponseProfileDTO getProfileByUserId(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<ResponseWatchListDTO> watchlist = watchListService.getWatchlistByUser(userId);
        List<ResponseFavoriteListDTO> favoriteList = favoriteListService.getFavoriteListByUser(userId);
        List<ResponseRatingDTO> ratings = ratingService.getRatingsByUser(userId);

        return ResponseProfileDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .watchlist(watchlist)
                .favoriteList(favoriteList)
                .ratings(ratings)
                .build();
    }
}
