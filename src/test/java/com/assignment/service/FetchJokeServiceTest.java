package com.assignment.service;

import com.assignment.dto.JokeApiResponseDTO;
import com.assignment.dto.JokeResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;


public class FetchJokeServiceTest {

    @InjectMocks
    private FetchJokeService fetchJokeService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Client client;

    @Mock
    private WebTarget webTarget;

    @Mock
    private Invocation.Builder invocationBuilder; // Mock for Invocation.Builder

    @Mock
    private Response response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    
    @Test
    void testFetchSingleJokeSuccess() throws Exception {
        // Mocking response and ObjectMapper behavior
        Client clientMock = mock(Client.class);
        WebTarget targetMock = mock(WebTarget.class);
        Invocation.Builder builderMock = mock(Invocation.Builder.class);
        Response responseMock = mock(Response.class);
        // Mock the API call response
        when(clientMock.target(fetchJokeService.JOKE_API_URL)).thenReturn(targetMock);
        when(targetMock.request("application/json")).thenReturn(builderMock);
        when(builderMock.get()).thenReturn(responseMock);
        when(responseMock.getStatus()).thenReturn(200);
        when(responseMock.readEntity(String.class)).thenReturn("{\"setup\": \"Why did the chicken...\", \"punchline\": \"To get to the other side!\"}");
        // Mock ObjectMapper
        JokeApiResponseDTO mockJokeApiResponse = new JokeApiResponseDTO();
        mockJokeApiResponse.setSetup("Why did the chicken...");
        mockJokeApiResponse.setPunchline("To get to the other side!");
        when(objectMapper.readValue(anyString(), eq(JokeApiResponseDTO.class))).thenReturn(mockJokeApiResponse);
        Uni<JokeResponseDTO> jokeUni = fetchJokeService.fetchSingleJoke();
        JokeResponseDTO jokeResponse = jokeUni.await().indefinitely();
        // Assertions
        assertNotNull(jokeResponse);
        assertEquals("Why did the chicken...", jokeResponse.getQuestion());
        assertEquals("To get to the other side!", jokeResponse.getAnswer());
    }
    @Test
    void testFetchSingleJokeFailure() {
        // Mocking failure scenario
        Client clientMock = mock(Client.class);
        WebTarget targetMock = mock(WebTarget.class);
        Invocation.Builder builderMock = mock(Invocation.Builder.class);
        Response responseMock = mock(Response.class);
        // Mock the API call response failure
        when(clientMock.target(fetchJokeService.JOKE_API_URL)).thenReturn(targetMock);
        when(targetMock.request("application/json")).thenReturn(builderMock);
        when(builderMock.get()).thenReturn(responseMock);
        when(responseMock.getStatus()).thenReturn(500); // Non-200 status
        // Expecting an exception to be thrown
        assertThrows(RuntimeException.class, () -> {
        	fetchJokeService.fetchSingleJoke().await().indefinitely();
        });
    }
    @Test
    void testFetchJokesBatch() throws Exception {
        // Mock fetchSingleJoke to return a specific joke for testing the batch process
        JokeResponseDTO joke1 = new JokeResponseDTO(1, "Question1", "Answer1","Type1");
        JokeResponseDTO joke2 = new JokeResponseDTO(2, "Question2", "Answer2","Type2");
        JokeResponseDTO joke3 = new JokeResponseDTO(3, "Question3", "Answer3","Type3");
        when(fetchJokeService.fetchSingleJoke()).thenReturn(Uni.createFrom().item(joke1), Uni.createFrom().item(joke2), Uni.createFrom().item(joke3));
        Uni<List<JokeResponseDTO>> jokeListUni = fetchJokeService.fetchJokes(3);
        // Assertions
        assertNotNull(jokeListUni);
    }

    @Test
    void testFetchSingleJokeJsonProcessingException() throws Exception {
        // Mocking the external dependencies: Client, WebTarget, Invocation.Builder, Response
        Client clientMock = mock(Client.class);
        WebTarget targetMock = mock(WebTarget.class);
        Invocation.Builder builderMock = mock(Invocation.Builder.class);
        Response responseMock = mock(Response.class);
        // Mocking the API call response
        when(clientMock.target(fetchJokeService.JOKE_API_URL)).thenReturn(targetMock);
        when(targetMock.request("application/json")).thenReturn(builderMock);
        when(builderMock.get()).thenReturn(responseMock);
        when(responseMock.getStatus()).thenReturn(200); // Simulate successful HTTP response
        // Simulate JSON response
        String jsonResponse = "{\"setup\": \"Why did the chicken...\", \"punchline\": \"To get to the other side!\"}";
        when(responseMock.readEntity(String.class)).thenReturn(jsonResponse);
        // Mock ObjectMapper to throw JsonProcessingException
        when(objectMapper.readValue(anyString(), eq(JokeApiResponseDTO.class)))
                .thenThrow(new JsonProcessingException("Error parsing JSON") {});
        // Expecting the RuntimeException to be thrown due to the JsonProcessingException
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
        	fetchJokeService.fetchSingleJoke().await().indefinitely();
        });
        // Assertions to ensure the catch block is properly executed
        assertNotNull(thrownException);
        assertEquals("Error deserializing joke response", thrownException.getMessage());
        assertTrue(thrownException.getCause() instanceof JsonProcessingException);
    }
    
    @Test
    public void testCreateJokeResponse() {
        // Arrange: Create a mock JokeApiResponseDTO object
        JokeApiResponseDTO jokeApiResponse = new JokeApiResponseDTO();
        jokeApiResponse.setId(1);
        jokeApiResponse.setSetup("Why did the chicken cross the road?");
        jokeApiResponse.setPunchline("To get to the other side.");
        jokeApiResponse.setType("general");

        // Act: Call the createJokeResponse method
        JokeResponseDTO jokeResponseDTO = fetchJokeService.createJokeResponse(jokeApiResponse);

        // Assert: Verify that the JokeResponseDTO fields match the JokeApiResponseDTO fields
        assertEquals(1, jokeResponseDTO.getId());
        assertEquals("Why did the chicken cross the road?", jokeResponseDTO.getQuestion());
        assertEquals("To get to the other side.", jokeResponseDTO.getAnswer());
        assertEquals("general", jokeResponseDTO.getType());
    }

}
