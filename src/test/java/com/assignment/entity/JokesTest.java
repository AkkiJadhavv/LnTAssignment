package com.assignment.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JokesTest {

    private Jokes jokes;

    @BeforeEach
    public void setUp() {
        jokes = new Jokes();
    }

    @Test
    public void testJokesDefaultConstructor() {
        assertNotNull(jokes); // Ensure that the default constructor creates an object
    }

    @Test
    public void testSetAndGetQuestion() {
        String testQuestion = "Why did the chicken cross the road?";
        jokes.setQuestion(testQuestion);
        assertEquals(testQuestion, jokes.getQuestion());
    }

    @Test
    public void testSetAndGetAnswer() {
        String testAnswer = "To get to the other side.";
        jokes.setAnswer(testAnswer);
        assertEquals(testAnswer, jokes.getAnswer());
    }
    @Test
    public void testSetAndGetTypee() {
        String type="type-1";
        jokes.setType(type);
        assertEquals(type, jokes.getType());
    }
    
    @Test
    public void testSetAndGetId() {
        long id =1L;
        jokes.setId(id);
        assertEquals(id, jokes.getId());
    }
}
