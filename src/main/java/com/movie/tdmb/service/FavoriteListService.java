package com.movie.tdmb.service;

import com.movie.tdmb.dto.RequestFavoriteListDTO;
import com.movie.tdmb.dto.ResponseFavoriteListDTO;
import com.movie.tdmb.exception.*;
import com.movie.tdmb.model.FavoriteList;
import com.movie.tdmb.model.Movie;
import com.movie.tdmb.repository.FavoriteListRepository;
import com.movie.tdmb.repository.MovieRepository;
import com.movie.tdmb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteListService {
    @Autowired
    private FavoriteListRepository favoriteListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieService movieService;

    public FavoriteList addFavoriteList(RequestFavoriteListDTO request, String userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        //System.out.print("ID: "+ request.getTmdbId());
        movieService.getMovieByTmdbId(request.getTmdbId());
        boolean exists = favoriteListRepository.findByTmdbIdAndUserId(request.getTmdbId(), userId).isPresent();
        if (exists) {
            throw new DuplicateWatchListException(
                    String.format("Movie with ID %s is already in the favorite list for user %s", request.getTmdbId(), userId));
        }
        FavoriteList favoriteList = FavoriteList.builder()
                .tmdbId(request.getTmdbId())
                .userId(userId)
                .addedAt(LocalDateTime.now())
                .build();
        FavoriteList savedFavoriteList = favoriteListRepository.save(favoriteList);
        return savedFavoriteList;
    }

    public List<ResponseFavoriteListDTO> getFavoriteListByUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<FavoriteList> favoriteLists = favoriteListRepository.findByUserId(userId);

        return favoriteLists.stream()
                .map(favoriteList -> {
                    System.out.println("ID: "+ favoriteList.getTmdbId());
                    Movie movie = movieService.getMovieByTmdbId(favoriteList.getTmdbId());

                    return ResponseFavoriteListDTO.builder()
                            .tmdbId(favoriteList.getTmdbId())
                            .addedAt(favoriteList.getAddedAt().toString())
                            .title(movie != null ? movie.getTitle() : null)
                            .posterPath(movie != null ? movie.getPoster_path() : null)
                            .overview(movie != null ? movie.getOverview() : null)
                            .releaseDate(movie != null ? movie.getRelease_date() : null)
                            .voteAverage(movie != null ? movie.getVote_average() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void removeFavoritelist(Long tmdbId, String userId) {
        FavoriteList favoriteList = favoriteListRepository.findByTmdbIdAndUserId(tmdbId, userId)
                .orElseThrow(() -> new FavoriteListNotFoundException("Favoritelist not found"));
        favoriteListRepository.delete(favoriteList);
    }
}