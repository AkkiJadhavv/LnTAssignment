package com.assignment.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class JokeRepositoryTest {

    private final JokeRepository jokeRepository = new JokeRepository();

    @Test
    void testJokeRepositoryIsNotNull() {
        // Ensure the repository instance is created and not null
        assertNotNull(jokeRepository);
    }
}