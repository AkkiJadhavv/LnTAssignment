package com.assignment.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JokeApiResponseDTOTest {

	@Test
    public void testRequestMethod() {
		JokeApiResponseDTO jokeApiResponseDTO = new JokeApiResponseDTO();
		
		jokeApiResponseDTO.equals(new JokeApiResponseDTO());
        jokeApiResponseDTO.hashCode();
        jokeApiResponseDTO.toString();
        assertNotNull(jokeApiResponseDTO.toString());
	}
	
	 @Test
	    void testGettersAndSetters() {
	        // Create an instance of JokeResponseDTO
		 JokeApiResponseDTO jokeApiResponseDTO = new JokeApiResponseDTO();

	        // Set values
		 jokeApiResponseDTO.setId(1);
		 jokeApiResponseDTO.setSetup("What did the fish say when it hit the wall?");
		 jokeApiResponseDTO.setPunchline("Dam!");
		 jokeApiResponseDTO.setType("general");

	        // Verify that the getters return the expected values
	        assertEquals(1, jokeApiResponseDTO.getId());
	        assertEquals("What did the fish say when it hit the wall?", jokeApiResponseDTO.getSetup());
	        assertEquals("Dam!", jokeApiResponseDTO.getPunchline());
	        assertEquals("general", jokeApiResponseDTO.getType());
	    }
}
