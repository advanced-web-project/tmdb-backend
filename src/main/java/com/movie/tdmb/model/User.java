package com.movie.tdmb.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Entity
@Document(value = "users")
@Data
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String profile;
    private Boolean isActive;
    private Date createdAt;
    private Date passwordChangedAt;
}
