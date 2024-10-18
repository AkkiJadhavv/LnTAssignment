package com.assignment.dto;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JokeSaveDTOTest {

	@Test
    public void testRequestMethod() {
		JokeSaveDTO jokeSaveDTO = new JokeSaveDTO();
		
		jokeSaveDTO.equals(new JokeSaveDTO());
        jokeSaveDTO.hashCode();
        jokeSaveDTO.toString();
        assertNotNull(jokeSaveDTO.toString());
	}
}
