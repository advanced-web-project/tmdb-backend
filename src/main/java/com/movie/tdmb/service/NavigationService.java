package com.movie.tdmb.service;

import com.movie.tdmb.dto.NavigationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class NavigationService {
    public NavigationResponse callExternalRetrieverAPI(String query, Float threshold) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String apiKey = System.getenv("GEMINI_API_KEY");
            String url = String.format(
                    "https://awd-llm.azurewebsites.net/navigate/?llm_api_key=%s&query=%s&threshold=%.2f",apiKey,
                    URLEncoder.encode(query, StandardCharsets.UTF_8), threshold
            );
            return restTemplate.postForObject(url, null, NavigationResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call external API");
        }
    }
}