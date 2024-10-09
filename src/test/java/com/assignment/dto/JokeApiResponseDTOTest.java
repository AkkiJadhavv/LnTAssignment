package com.assignment.dto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assignemnt.dto.JokeApiResponseDTO;

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
}
