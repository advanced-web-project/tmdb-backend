package com.movie.tdmb.controller;

import com.movie.tdmb.dto.RequestFavoriteListDTO;
import com.movie.tdmb.dto.ResponseFavoriteListDTO;
import com.movie.tdmb.model.FavoriteList;
import com.movie.tdmb.security.jwt.JwtUtils;
import com.movie.tdmb.service.FavoriteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritelists")
public class FavoriteListController {
    @Autowired
    private FavoriteListService favoriteListService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<FavoriteList> addFavoriteList(@RequestBody RequestFavoriteListDTO request, @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        FavoriteList response = favoriteListService.addFavoriteList(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ResponseFavoriteListDTO>> getFavoriteListByUser(@RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        List<ResponseFavoriteListDTO> response = favoriteListService.getFavoriteListByUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{tmdbId}")
    public ResponseEntity<Void> removeFavoriteList(@PathVariable Long tmdbId, @RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        favoriteListService.removeFavoritelist(tmdbId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}