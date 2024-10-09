package com.assignment.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assignemnt.entity.Jokes;

@ExtendWith(MockitoExtension.class)
public class JokesTest {

	 @Test
	    public void setup() {
		 
		 Jokes jokes = new Jokes();
		 jokes.setAnswer("He just took it in stride");
		 jokes.setId(93L);
		 jokes.setQuestion("Did you hear about the runner who was criticized?");
	 }
}
