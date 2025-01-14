package com.movie.tdmb.controller;

import com.movie.tdmb.dto.ResponseProfileDTO;
import com.movie.tdmb.model.User;
import com.movie.tdmb.security.jwt.JwtUtils;
import com.movie.tdmb.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<ResponseProfileDTO> getProfile(@RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        ResponseProfileDTO response = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserByAuthorization(@RequestHeader("Authorization") String token) {
        String userId = jwtUtils.getIdFromJwtToken(token.substring(7));
        User user = profileService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }
}
