package com.movie.tdmb.repository;
import com.movie.tdmb.model.UserHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserHistoryRepository extends MongoRepository<UserHistory, String> {
    Optional<UserHistory> findByUserId(String userId);
}
