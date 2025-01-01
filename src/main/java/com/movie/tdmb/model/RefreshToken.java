package com.movie.tdmb.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "refresh_tokens")
@RequiredArgsConstructor
public class RefreshToken {
    @Id
    private String id;
    private String userId;
    private String token;
    private Instant expiryDate;
    public void setExpiryDate() {
        this.expiryDate = Instant.now().plusSeconds(864000); // 10 days in seconds
    }
}
