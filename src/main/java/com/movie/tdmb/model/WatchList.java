package com.movie.tdmb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Entity
@Document(value = "watchlists")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {
    @Id
    private String id;
    private String movieId;
    private String userId;
    private LocalDateTime addedAt;
}