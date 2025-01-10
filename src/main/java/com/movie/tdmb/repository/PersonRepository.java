package com.movie.tdmb.repository;

import com.movie.tdmb.model.Movie;
import com.movie.tdmb.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PersonRepository extends MongoRepository<Person, String> {
    Page<Person> findAll(Pageable pageable);
    Optional<Person> findById(String id);
}
