package com.movie.tdmb.service;

import com.movie.tdmb.dto.RequestFavoriteListDTO;
import com.movie.tdmb.dto.RequestRatingDTO;
import com.movie.tdmb.dto.ResponseRatingDTO;
import com.movie.tdmb.dto.ResponseWatchListDTO;
import com.movie.tdmb.exception.DuplicateRatingException;
import com.movie.tdmb.exception.MovieNotFoundException;
import com.movie.tdmb.exception.RatingNotFoundException;
import com.movie.tdmb.exception.UserNotFoundException;
import com.movie.tdmb.model.Movie;
import com.movie.tdmb.model.Rating;
import com.movie.tdmb.repository.MovieRepository;
import com.movie.tdmb.repository.RatingRepository;
import com.movie.tdmb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public Rating addRating(RequestRatingDTO request, String userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        movieRepository.findById(request.getMovieId()).orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        boolean exists = ratingRepository.findByMovieIdAndUserId(request.getMovieId(), userId).isPresent();
        if (exists) {
            throw new DuplicateRatingException(
                    String.format("Rating for movie with ID %s already exists for user %s", request.getMovieId(), userId));
        }
        Rating rating = Rating.builder()
                .movieId(request.getMovieId())
                .userId(userId)
                .score(request.getScore())
                .ratedAt(LocalDateTime.now())
                .build();
        Rating savingRating = ratingRepository.save(rating);
        return savingRating;
    }

    public void deleteRating(String movieId, String userId) {
        Rating rating = ratingRepository.findByMovieIdAndUserId(movieId, userId)
                .orElseThrow(() -> new RatingNotFoundException("Rating not found"));
        ratingRepository.delete(rating);
    }

    public void updateRating(RequestRatingDTO request, String userId) {
        Rating rating = ratingRepository.findByMovieIdAndUserId(request.getMovieId(), userId)
                .orElseThrow(() -> new RatingNotFoundException("Rating not found"));
        rating.setScore(request.getScore());
        rating.setRatedAt(LocalDateTime.now());
        ratingRepository.save(rating);
    }

    public List<ResponseRatingDTO> getRatingsByUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Rating> ratings = ratingRepository.findByUserId(userId);

        return ratings.stream()
                .map(rating -> {
                    Movie movie = movieRepository.findById(rating.getMovieId()).orElse(null);

                    return ResponseRatingDTO.builder()
                            .movieId(rating.getMovieId())
                            .score(rating.getScore())
                            .ratedAt(rating.getRatedAt().toString())
                            .title(movie != null ? movie.getTitle() : null)
                            .posterPath(movie != null ? movie.getPoster_path() : null)
                            .overview(movie != null ? movie.getOverview() : null)
                            .releaseDate(movie != null ? movie.getRelease_date() : null)
                            .voteAverage(movie != null ? movie.getVote_average() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
