package com.assignment.repository;

import com.assignment.entity.Jokes;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JokeRepository implements PanacheRepository<Jokes> {

}