package com.assignment.service;

import java.util.ArrayList;
import java.util.List;
import com.assignment.dto.JokeApiResponseDTO;
import com.assignment.dto.JokeResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Slf4j
@ApplicationScoped
public class FetchJokeService {

    @Inject
    ObjectMapper objectMapper;

    public static final String JOKE_API_URL = "http://official-joke-api.appspot.com/random_joke";

    public Uni<List<JokeResponseDTO>> fetchJokes(int batchSize) {
        log.info("Entering fetchJokes method");
        List<Uni<JokeResponseDTO>> jokeFetchers = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            jokeFetchers.add(fetchSingleJoke());
        }

        // Use Multi to handle the unis and collect them into a list
     // Use Multi to handle the Unis and merge them into a single stream
        return Multi.createFrom().iterable(jokeFetchers) // Create a Multi<Uni<JokeResponseDTO>>
                .onItem().transformToUniAndMerge(joke -> joke) // Merge Uni<JokeResponseDTO> into Multi<JokeResponseDTO>
                .collect().asList(); // Collect the Multi<JokeResponseDTO> into a Uni<List<JokeResponseDTO>>
    }



     Uni<JokeResponseDTO> fetchSingleJoke() {
        return Uni.createFrom().item(() -> {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(JOKE_API_URL);
            Response response = target.request("application/json").get();

            if (response.getStatus() == 200) {
                String jsonResponse = response.readEntity(String.class);
                try {
                    JokeApiResponseDTO jokeApiResponse = objectMapper.readValue(jsonResponse, JokeApiResponseDTO.class);
                    return createJokeResponse(jokeApiResponse);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error deserializing joke response", e);
                }
            } else {
                throw new RuntimeException("Failed to fetch joke");
            }
        });
    }
     
     

    JokeResponseDTO createJokeResponse(JokeApiResponseDTO jokeApiResponse) {
        JokeResponseDTO jokeResponseDTO = new JokeResponseDTO();
        jokeResponseDTO.setId(jokeApiResponse.getId());
        jokeResponseDTO.setQuestion(jokeApiResponse.getSetup());
        jokeResponseDTO.setAnswer(jokeApiResponse.getPunchline());
        jokeResponseDTO.setType(jokeApiResponse.getType());
        return jokeResponseDTO;
    }
}
