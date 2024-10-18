package com.assignment.service;

import com.assignment.dto.JokeResponseDTO;
import com.assignment.entity.Jokes;
import com.assignment.repository.JokeRepository;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JokeSaveServiceTest {

    @Mock
    JokeRepository jokeRepository;

    @Mock
    FetchJokeService fetchJokeService;

    @Mock
    JokeExistanceService jokeExistanceService;

    @InjectMocks
    JokeSaveService jokeSaveService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveJokes_Success() {
        // Given
        int count = 10;
        List<JokeResponseDTO> fetchedJokes = List.of(new JokeResponseDTO(/* initialize your joke object */));
        
        when(fetchJokeService.fetchJokes(10)).thenReturn(Uni.createFrom().item(fetchedJokes));
        when(jokeExistanceService.jokeExistsInDatabase(anyLong())).thenReturn(Uni.createFrom().item(false));
        when(jokeRepository.persist(any(Jokes.class))).thenReturn(Uni.createFrom().item(new Jokes()));

        // When
        Uni<List<JokeResponseDTO>> result = jokeSaveService.saveJokes(count);
        
        // Then
        List<JokeResponseDTO> jokes = result.await().indefinitely();
        assertEquals(1, jokes.size());
    }

    @Test
    public void testSaveJokes_NoJokesFetched() {
        // Given
        int count = 10;
        
        when(fetchJokeService.fetchJokes(10)).thenReturn(Uni.createFrom().item(Collections.emptyList()));

        // When
        Uni<List<JokeResponseDTO>> result = jokeSaveService.saveJokes(count);
        
        // Then
        List<JokeResponseDTO> jokes = result.await().indefinitely();
        assertEquals(0, jokes.size());
    }

    @Test
    public void testSaveJokes_JokeExistsInDatabase() {
        // Given
        int count = 10;
        List<JokeResponseDTO> fetchedJokes = List.of(new JokeResponseDTO(/* initialize your joke object */));

        when(fetchJokeService.fetchJokes(10)).thenReturn(Uni.createFrom().item(fetchedJokes));
        when(jokeExistanceService.jokeExistsInDatabase(anyLong())).thenReturn(Uni.createFrom().item(true)); // Joke already exists

        // When
        Uni<List<JokeResponseDTO>> result = jokeSaveService.saveJokes(count);
        
        // Then
        List<JokeResponseDTO> jokes = result.await().indefinitely();
        assertEquals(1, jokes.size()); // Should still return the joke even if it was not saved
    }

    @Test
    public void testSaveJokes_ExceptionWhileFetching() {
        // Given
        int count = 10;

        when(fetchJokeService.fetchJokes(10)).thenReturn(Uni.createFrom().failure(new RuntimeException("Fetch error")));

        // When
        Uni<List<JokeResponseDTO>> result = jokeSaveService.saveJokes(count);
        
        // Then
        result.onFailure().invoke(Throwable::printStackTrace).subscribe().with(
            ignored -> {
                // This will be ignored as we're expecting a failure
            },
            failure -> {
                assertEquals("Fetch error", failure.getMessage());
            }
        );
    }

    @Test
    public void testSaveJokes_ExceptionWhilePersisting() {
        // Given
        int count = 10;
        List<JokeResponseDTO> fetchedJokes = List.of(new JokeResponseDTO(/* initialize your joke object */));

        when(fetchJokeService.fetchJokes(10)).thenReturn(Uni.createFrom().item(fetchedJokes));
        when(jokeExistanceService.jokeExistsInDatabase(anyLong())).thenReturn(Uni.createFrom().item(false));
        when(jokeRepository.persist(any(Jokes.class))).thenReturn(Uni.createFrom().failure(new RuntimeException("Persist error")));

        // When
        Uni<List<JokeResponseDTO>> result = jokeSaveService.saveJokes(count);
        
        // Then
        result.onFailure().invoke(Throwable::printStackTrace).subscribe().with(
            ignored -> {
                // This will be ignored as we're expecting a failure
            },
            failure -> {
                assertEquals("Persist error", failure.getMessage());
            }
        );
    }

}
