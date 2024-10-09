package com.assignment.dto;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assignemnt.dto.JokeResponseDTO;


@ExtendWith(MockitoExtension.class)
public class JokeResponseDTOTest {

	@Test
    public void testRequestMethod() {
		JokeResponseDTO jokeResponseDTO = new JokeResponseDTO();
		
		jokeResponseDTO.equals(new JokeResponseDTO());
        jokeResponseDTO.hashCode();
        jokeResponseDTO.toString();
        assertNotNull(jokeResponseDTO.toString());
	}
}
