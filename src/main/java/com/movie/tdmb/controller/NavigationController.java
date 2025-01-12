package com.movie.tdmb.controller;

import com.movie.tdmb.dto.NavigationResponse;
import com.movie.tdmb.service.NavigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/navigation")
@RequiredArgsConstructor
public class NavigationController {
    private final NavigationService navigationService;
    /**
     * This method is used to call the external retriever API to get the navigation data based on the query of the user.
     * @param query
     * @param threshold
     * @return
     */
    @GetMapping()
    public ResponseEntity<?> NavigationAI(@RequestParam("query") String query, @RequestParam(value = "threshold", required = false) Float threshold) {
        NavigationResponse navigationResponse = navigationService.callExternalRetrieverAPI(query, threshold);
        return ResponseEntity.ok(navigationResponse.getData());
    }
}
