package com.movie.tdmb.repository;
import com.movie.tdmb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByUsername(String userName);
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
}
