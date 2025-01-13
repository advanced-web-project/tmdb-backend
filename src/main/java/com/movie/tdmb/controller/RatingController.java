package com.movie.tdmb.controller;

import com.movie.tdmb.dto.RequestRatingDTO;
import com.movie.tdmb.dto.ResponseRatingDTO;
import com.movie.tdmb.model.Rating;
import com.movie.tdmb.security.jwt.JwtUtils;
import com.movie.tdmb.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestBody RequestRatingDTO request, @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        Rating response = ratingService.addRating(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ResponseRatingDTO>> getRatingsByUser(@RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        List<ResponseRatingDTO> response = ratingService.getRatingsByUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long movieId, @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        ratingService.deleteRating(movieId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> updateRating(@RequestBody RequestRatingDTO request, @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        ratingService.updateRating(request, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
