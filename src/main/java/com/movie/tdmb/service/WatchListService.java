package com.movie.tdmb.service;

import com.movie.tdmb.dto.RequestWatchListDTO;
import com.movie.tdmb.dto.ResponseWatchListDTO;
import com.movie.tdmb.exception.DuplicateWatchListException;
import com.movie.tdmb.exception.MovieNotFoundException;
import com.movie.tdmb.exception.UserNotFoundException;
import com.movie.tdmb.exception.WatchListNotFoundException;
import com.movie.tdmb.model.Movie;
import com.movie.tdmb.model.WatchList;
import com.movie.tdmb.repository.MovieRepository;
import com.movie.tdmb.repository.UserRepository;
import com.movie.tdmb.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchListService {
    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieService movieService;

    public WatchList addWatchlist(RequestWatchListDTO request, String userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        movieService.getMovieByTmdbId(request.getTmdbId());
        boolean exists = watchListRepository.findByTmdbIdAndUserId(request.getTmdbId(), userId).isPresent();
        if (exists) {
            throw new DuplicateWatchListException(
                    String.format("Movie with ID %s is already in the watchlist for user %s", request.getTmdbId(), userId));
        }
        WatchList watchlist = WatchList.builder()
                .tmdbId(request.getTmdbId())
                .userId(userId)
                .addedAt(LocalDateTime.now())
                .build();
        WatchList savedWatchlist = watchListRepository.save(watchlist);
        return savedWatchlist;
    }

    public List<ResponseWatchListDTO> getWatchlistByUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<WatchList> watchlists = watchListRepository.findByUserId(userId);

        return watchlists.stream()
                .map(watchlist -> {
                    Movie movie = movieService.getMovieByTmdbId(watchlist.getTmdbId());

                    return ResponseWatchListDTO.builder()
                            .tmdbId(watchlist.getTmdbId())
                            .addedAt(watchlist.getAddedAt().toString())
                            .title(movie != null ? movie.getTitle() : null)
                            .posterPath(movie != null ? movie.getPoster_path() : null)
                            .overview(movie != null ? movie.getOverview() : null)
                            .releaseDate(movie != null ? movie.getRelease_date() : null)
                            .voteAverage(movie != null ? movie.getVote_average() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void removeWatchlist(Long tmdbId, String userId) {
        WatchList watchlist = watchListRepository.findByTmdbIdAndUserId(tmdbId, userId)
                .orElseThrow(() -> new WatchListNotFoundException("Watchlist not found"));
        watchListRepository.delete(watchlist);
    }
}
