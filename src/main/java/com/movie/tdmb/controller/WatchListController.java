package com.movie.tdmb.controller;

import com.movie.tdmb.dto.RequestWatchListDTO;
import com.movie.tdmb.dto.ResponseWatchListDTO;
import com.movie.tdmb.model.WatchList;
import com.movie.tdmb.security.jwt.JwtUtils;
import com.movie.tdmb.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlists")
public class WatchListController {
    @Autowired
    private WatchListService watchListService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<WatchList> addWatchlist(@RequestBody RequestWatchListDTO request, @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        WatchList response = watchListService.addWatchlist(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ResponseWatchListDTO>> getWatchlistByUser(@RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        List<ResponseWatchListDTO> response = watchListService.getWatchlistByUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{tmdbId}")
    public ResponseEntity<Void> removeWatchlist(@PathVariable Long tmdbId, @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        watchListService.removeWatchlist(tmdbId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
