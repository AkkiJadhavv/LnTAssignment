package com.assignment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.assignemnt.entity.Jokes;
import com.assignemnt.repository.JokeRepository;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import io.smallrye.mutiny.Uni;

@QuarkusTest
public class JokeRepositoryTest {

    @Inject
    JokeRepository jokeRepository;

    @Test
    @TestTransaction
    public void testSaveAndRetrieveJoke() {
        // Create a new joke
        Jokes joke = new Jokes();
        joke.setQuestion("Why don't skeletons fight each other?");
        joke.setAnswer("Because they don't have the guts.");
        
        // Save the joke
        jokeRepository.persist(joke).await().indefinitely();
        
        // Ensure it was saved by retrieving it
        Uni<Jokes> retrievedJoke = jokeRepository.findById(joke.getId());
        Jokes result = retrievedJoke.await().indefinitely();
        
        assertNotNull(result);
        assertEquals(joke.getQuestion(), result.getQuestion());
        assertEquals(joke.getAnswer(), result.getAnswer());
    }

    @Test
    @TestTransaction
    public void testFindAllJokes() {
        // Ensure the database is initially empty
        List<Jokes> jokesBefore = jokeRepository.findAll().list().await().indefinitely();
        assertEquals(0, jokesBefore.size());

        // Add a few jokes
        Jokes joke1 = new Jokes();
        joke1.setQuestion("Why did the chicken join a band?");
        joke1.setAnswer("Because it had the drumsticks.");
        jokeRepository.persist(joke1).await().indefinitely();

        Jokes joke2 = new Jokes();
        joke2.setQuestion("What do you call fake spaghetti?");
        joke2.setAnswer("An impasta.");
        jokeRepository.persist(joke2).await().indefinitely();

        // Retrieve all jokes and verify
        List<Jokes> jokesAfter = jokeRepository.findAll().list().await().indefinitely();
        assertEquals(2, jokesAfter.size());
    }

    @Test
    @TestTransaction
    public void testDeleteJoke() {
        // Create and save a new joke
        Jokes joke = new Jokes();
        joke.setQuestion("Why did the scarecrow win an award?");
        joke.setAnswer("Because he was outstanding in his field.");
        jokeRepository.persist(joke).await().indefinitely();

        // Verify it was saved
        Jokes retrievedJoke = jokeRepository.findById(joke.getId()).await().indefinitely();
        assertNotNull(retrievedJoke);

        // Delete the joke
        jokeRepository.delete(retrievedJoke).await().indefinitely();

        // Verify it was deleted
        Jokes deletedJoke = jokeRepository.findById(joke.getId()).await().indefinitely();
        assertEquals(null, deletedJoke);
    }
}
