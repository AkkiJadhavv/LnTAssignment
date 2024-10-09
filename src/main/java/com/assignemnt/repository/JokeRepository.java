package com.assignemnt.repository;

import com.assignemnt.entity.Jokes;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JokeRepository implements PanacheRepository<Jokes> {
}

