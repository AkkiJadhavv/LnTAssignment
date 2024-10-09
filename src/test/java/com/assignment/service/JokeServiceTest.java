package com.assignment.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.assignemnt.dto.JokeApiResponseDTO;
import com.assignemnt.dto.JokeResponseDTO;
import com.assignemnt.dto.JokeSaveDTO;
import com.assignemnt.entity.Jokes;
import com.assignemnt.repository.JokeRepository;
import com.assignemnt.service.JokeService;

import io.reactivex.rxjava3.core.Single;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class JokeServiceTest {

	 @InjectMocks
	    JokeService jokeService;

	    @Mock
	    JokeRepository jokeRepository;

	    @Mock
	    Client client;

	    @BeforeEach
	    void init() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testGetJokesSuccess() {
	        // Mock API response
	        JokeApiResponseDTO mockJoke = new JokeApiResponseDTO();
	        mockJoke.setSetup("Why did the chicken cross the road?");
	        mockJoke.setPunchline("To get to the other side.");
	        
	        when(client.target(anyString())).thenReturn((WebTarget) mock(WebTarget.class));
	        when(client.target(anyString()).request(MediaType.APPLICATION_JSON).get(JokeApiResponseDTO.class))
	                .thenReturn(mockJoke);

	        // Call the method
	        Single<List<JokeResponseDTO>> result = jokeService.getJokes(1);
	        List<JokeResponseDTO> jokes = result.blockingGet();

	        // Verify behavior
	        verify(jokeRepository, times(1)).persist(any(Jokes.class));
	        assertEquals(1, jokes.size());
	        assertEquals(mockJoke.getSetup(), jokes.get(0).getQuestion());
	        assertEquals(mockJoke.getPunchline(), jokes.get(0).getAnswer());
	    }

	    @Test
	    public void testGetJokesFailure() {
	        // Simulate API failure
	        when(client.target(anyString())).thenThrow(new RuntimeException("API failure"));

	        // Call the method
	        Single<List<JokeResponseDTO>> result = jokeService.getJokes(1);

	        // Assert that an error is thrown
	        Exception exception = assertThrows(RuntimeException.class, result::blockingGet);
	        assertEquals("API failure", exception.getMessage());
	    }

	    @Test
	    public void testSaveJokeSuccess() {
	        JokeSaveDTO jokeSaveDTO = new JokeSaveDTO();
	        jokeSaveDTO.setQuestion("Test Question");
	        jokeSaveDTO.setAnswer("Test Answer");

	        Jokes savedJoke = new Jokes();
	        savedJoke.setId(1L);
	        savedJoke.setQuestion("Test Question");
	        savedJoke.setAnswer("Test Answer");

	        when(jokeRepository.persist(any(Jokes.class))).thenAnswer(invocation -> {
	            Jokes jokeEntity = invocation.getArgument(0);
	            jokeEntity.setId(savedJoke.getId());
	            return null;
	        });

	        JokeResponseDTO response = jokeService.saveJoke(jokeSaveDTO).blockingGet();

	        verify(jokeRepository).persist(any(Jokes.class));
	        assertEquals("1", response.getId());
	        assertEquals(jokeSaveDTO.getQuestion(), response.getQuestion());
	        assertEquals(jokeSaveDTO.getAnswer(), response.getAnswer());
	    }

	    @Test
	    public void testSaveJokeFailure() {
	        JokeSaveDTO jokeSaveDTO = new JokeSaveDTO();
	        jokeSaveDTO.setQuestion("Test Question");
	        jokeSaveDTO.setAnswer("Test Answer");

	        // Simulate persistence failure
	        doThrow(new RuntimeException("Database error")).when(jokeRepository).persist(any(Jokes.class));

	        // Call the method
	        Single<JokeResponseDTO> result = jokeService.saveJoke(jokeSaveDTO);

	        // Assert that an error is thrown
	        Exception exception = assertThrows(RuntimeException.class, result::blockingGet);
	        assertEquals("Database error", exception.getMessage());
	    }
}
