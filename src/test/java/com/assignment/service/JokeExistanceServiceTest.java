package com.assignment.service;

import com.assignment.entity.Jokes;  // Ensure you import the actual Jokes entity class
import com.assignment.repository.JokeRepository;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JokeExistanceServiceTest {

    @InjectMocks
    private JokeExistanceService jokeExistanceService;

    @Mock
    private JokeRepository jokeRepository;

    @BeforeEach
    void setUp() {
        // This method runs before each test case to set up any necessary context
    }

    @Test
    void testJokeExistsInDatabase_Positive() {
        // Given
        long jokeId = 1L;
        Jokes mockJoke = new Jokes(); // Create a mock joke object
        mockJoke.setId(jokeId); // Set the ID to match the lookup
        when(jokeRepository.findById(jokeId)).thenReturn(Uni.createFrom().item(mockJoke)); // Mock an existing joke

        // When
        Uni<Boolean> result = jokeExistanceService.jokeExistsInDatabase(jokeId);

        // Then
        assertEquals(true, result.await().indefinitely());
        verify(jokeRepository, times(1)).findById(jokeId); // Verify the repository was called once
    }

    @Test
    void testJokeExistsInDatabase_Negative() {
        // Given
        long jokeId = 2L;
        when(jokeRepository.findById(jokeId)).thenReturn(Uni.createFrom().nullItem()); // Mock a non-existing joke

        // When
        Uni<Boolean> result = jokeExistanceService.jokeExistsInDatabase(jokeId);

        // Then
        assertEquals(false, result.await().indefinitely()); // Expect false since the joke doesn't exist
        verify(jokeRepository, times(1)).findById(jokeId); // Verify the repository was called once
    }
}
