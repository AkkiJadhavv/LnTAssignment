package com.assignemnt.service;


import io.reactivex.rxjava3.core.Single;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import com.assignemnt.dto.JokeApiResponseDTO;
import com.assignemnt.dto.JokeResponseDTO;
import com.assignemnt.dto.JokeSaveDTO;
import com.assignemnt.entity.Jokes;
import com.assignemnt.repository.JokeRepository;

@ApplicationScoped
public class JokeService {

    private static final String JOKE_API_URL = "https://official-joke-api.appspot.com/random_joke";

    @Inject
    JokeRepository jokeRepository;

    public Single<List<JokeResponseDTO>> getJokes(int count) {
        Client client = ClientBuilder.newClient();
        return Single.create(emitter -> {
            List<JokeResponseDTO> jokes = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                WebTarget target = client.target(JOKE_API_URL);
                JokeApiResponseDTO joke = target.request(MediaType.APPLICATION_JSON).get(JokeApiResponseDTO.class);
                JokeResponseDTO jokeResponseDTO = new JokeResponseDTO();
                jokeResponseDTO.setId(java.util.UUID.randomUUID().toString());
                jokeResponseDTO.setQuestion(joke.getSetup());
                jokeResponseDTO.setAnswer(joke.getPunchline());

                jokeRepository.persist(convertToEntity(jokeResponseDTO)); // Save to DB
                jokes.add(jokeResponseDTO); // Collect the joke
            }
            emitter.onSuccess(jokes); // Emit the result
            client.close();
        });
    }

    private Jokes convertToEntity(JokeResponseDTO jokeResponseDTO) {
        Jokes jokes = new Jokes();
        jokes.setQuestion(jokeResponseDTO.getQuestion());
        jokes.setAnswer(jokeResponseDTO.getAnswer());
        return jokes;
    }

    public Single<JokeResponseDTO> saveJoke(JokeSaveDTO jokeSaveDTO) {
        Jokes jokes = new Jokes();
        jokes.setQuestion(jokeSaveDTO.getQuestion());
        jokes.setAnswer(jokeSaveDTO.getAnswer());
        jokeRepository.persist(jokes);
        
        JokeResponseDTO jokeResponseDTO = new JokeResponseDTO();
        jokeResponseDTO.setId(String.valueOf(jokes.getId()));
        jokeResponseDTO.setQuestion(jokes.getQuestion());
        jokeResponseDTO.setAnswer(jokes.getAnswer());
        
        return Single.just(jokeResponseDTO);
    }
}
