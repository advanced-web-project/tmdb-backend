package com.movie.tdmb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Entity
@Document(value = "favoritelists")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteList {
    @Id
    private String id;
    private Long tmdbId;
    private String userId;
    private LocalDateTime addedAt;
}
