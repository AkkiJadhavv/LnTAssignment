package com.assignment.dto;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JokeResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        // Create an instance of JokeResponseDTO
        JokeResponseDTO jokeResponseDTO = new JokeResponseDTO();

        // Set values
        jokeResponseDTO.setId(1);
        jokeResponseDTO.setQuestion("What did the fish say when it hit the wall?");
        jokeResponseDTO.setAnswer("Dam!");
        jokeResponseDTO.setType("general");

        // Verify that the getters return the expected values
        assertEquals(1, jokeResponseDTO.getId());
        assertEquals("What did the fish say when it hit the wall?", jokeResponseDTO.getQuestion());
        assertEquals("Dam!", jokeResponseDTO.getAnswer());
        assertEquals("general", jokeResponseDTO.getType());
    }
    
    @Test
  public void testRequestMethod() {
	  JokeResponseDTO jokeResponseDTO = new JokeResponseDTO();
		
	  jokeResponseDTO.equals(new JokeResponseDTO());
      jokeResponseDTO.hashCode();
      jokeResponseDTO.toString();
      assertNotNull(jokeResponseDTO.toString());
	}
}

