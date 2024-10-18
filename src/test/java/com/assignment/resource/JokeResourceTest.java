package com.assignment.resource;

import com.assignment.dto.JokeResponseDTO;
import com.assignment.service.JokeSaveService;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class JokeResourceTest {

    @Mock
    JokeSaveService jokeService;

    @InjectMocks
    JokeResource jokeResource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchJokes_Success() {
        // Given
        int count = 5;
        List<JokeResponseDTO> jokes = List.of(new JokeResponseDTO(/* initialize your joke object */));
        
        when(jokeService.saveJokes(count)).thenReturn(Uni.createFrom().item(jokes));

        // When
        Uni<Response> responseUni = jokeResource.fetchJokes(count);
        
        // Then
        Response response = responseUni.await().indefinitely();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(jokes, response.getEntity());
    }

    @Test
    public void testFetchJokes_NoJokesFound() {
        // Given
        int count = 5;
        when(jokeService.saveJokes(count)).thenReturn(Uni.createFrom().item(Collections.emptyList()));

        // When
        Uni<Response> responseUni = jokeResource.fetchJokes(count);
        
        // Then
        Response response = responseUni.await().indefinitely();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("No jokes found", response.getEntity());
    }

    @Test
    public void testFetchJokes_BadRequest() {
        // Given
        int count = 0; // invalid count

        // When
        Uni<Response> responseUni = jokeResource.fetchJokes(count);
        
        // Then
        Response response = responseUni.await().indefinitely();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Count must be between 1 and 100", response.getEntity());
    }

    @Test
    public void testFetchJokes_ExceptionOccurred() {
        // Given
        int count = 5;
        when(jokeService.saveJokes(count)).thenReturn(Uni.createFrom().failure(new RuntimeException("Error occurred")));

        // When
        Uni<Response> responseUni = jokeResource.fetchJokes(count);
        
        // Then
        Response response = responseUni.await().indefinitely();
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Failed to process request", response.getEntity());
    }
}
