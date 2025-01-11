package com.movie.tdmb.service;

import com.movie.tdmb.dto.DataPageResponse;
import com.movie.tdmb.exception.MovieNotFoundException;
import com.movie.tdmb.exception.PersonNotFoundException;
import com.movie.tdmb.model.Person;
import com.movie.tdmb.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public DataPageResponse getPersons(Pageable pageable) {
        Page<Person> pages = personRepository.findAll(pageable);
        return DataPageResponse.builder()
                .page(pages.getNumber())
                .totalResults((int) pages.getTotalElements())
                .perPage(pages.getSize())
                .totalPages(pages.getTotalPages())
                .data(pages.getContent())
                .build();
    }

    public Person getPersonById(Long id) {
        return personRepository.findByTmdbId(id).orElseThrow(() -> new PersonNotFoundException("Person not found"));
    }
}
