package com.assignment.service;

import com.assignment.repository.JokeRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class JokeExistanceService {

    @Inject
    JokeRepository jokeRepository;

    public Uni<Boolean> jokeExistsInDatabase(long id) {
    	log.info("Entering in jokeExistsInDatabase ");
        return jokeRepository.findById(id)
            .onItem().transform(joke -> joke != null);  // Return true if joke exists, false otherwise
    }
}
