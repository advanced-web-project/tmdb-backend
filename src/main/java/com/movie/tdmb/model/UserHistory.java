package com.movie.tdmb.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "user_history")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserHistory {
    @Id
    private String id;
    private String userId;
    private Map<Long,Integer> movies;
}
