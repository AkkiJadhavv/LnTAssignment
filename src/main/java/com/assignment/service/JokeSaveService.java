package com.assignment.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import com.assignment.dto.JokeResponseDTO;
import com.assignment.entity.Jokes;
import com.assignment.repository.JokeRepository;
import io.smallrye.mutiny.Uni;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Multi;

@Slf4j
@ApplicationScoped
public class JokeSaveService {

    @Inject
    JokeRepository jokeRepository;

    @Inject
    FetchJokeService fetchJokeService;

    @Inject
    JokeExistanceService jokeExistanceService;

    @Transactional
    public Uni<List<JokeResponseDTO>> saveJokes(int count) {
        log.info("Entering in saveJokes method from service class");

        // Calculate total number of batches needed
        int totalBatches = (int) Math.ceil(count / 10.0);

        // Create a list of Uni<List<JokeResponseDTO>> for parallel execution of batches
        List<Uni<List<JokeResponseDTO>>> batchFetchers = new ArrayList<>();
        for (int i = 0; i < totalBatches; i++) {
            int batchSize = Math.min(count - i * 10, 10); // Batch size is 10 or less in the last batch
            batchFetchers.add(fetchAndSaveBatch(batchSize));
        }

        // Use Multi to process each batch in parallel and flatten the results into a single list
        return Multi.createFrom().iterable(batchFetchers) // Creates a Multi<Uni<List<JokeResponseDTO>>>
                .onItem().transformToUniAndMerge(batchUni -> batchUni) // Flatten Uni<List<JokeResponseDTO>> into List<JokeResponseDTO>
                .onItem().transformToMulti(jokesList -> Multi.createFrom().iterable(jokesList)) // Flatten List<JokeResponseDTO> into individual JokeResponseDTO
                .merge()
                .collect().asList() // Collect all JokeResponseDTO into a single list
                .onItem().invoke(jokes -> log.info("Exiting saveJokes method from service class with {} jokes", jokes.size()));
    }

    @WithSession
    Uni<List<JokeResponseDTO>> fetchAndSaveBatch(int batchSize) {
        return fetchJokeService.fetchJokes(batchSize)  // Fetch a batch of jokes
            .onItem().transformToUni(jokes -> Multi.createFrom().iterable(jokes)
                .onItem().transformToUniAndMerge(joke -> saveJokeIfNotExists(joke)) // Save each joke if it doesn't exist
                .collect().asList()); // Collect all jokes
    }

    private Uni<JokeResponseDTO> saveJokeIfNotExists(JokeResponseDTO jokeResponseDTO) {
        return jokeExistanceService.jokeExistsInDatabase(jokeResponseDTO.getId())
            .onItem().transformToUni(exists -> {
                if (!exists) {
                    Jokes jokeEntity = convertToEntity(jokeResponseDTO);
                    return jokeRepository.persist(jokeEntity)
                        .onItem().transform(ignored -> jokeResponseDTO);
                } else {
                    return Uni.createFrom().item(jokeResponseDTO);
                }
            });
    }

    private Jokes convertToEntity(JokeResponseDTO jokeResponseDTO) {
        log.info("Converting JokeResponseDTO to entity");
        Jokes joke = new Jokes();
        joke.setId(jokeResponseDTO.getId());
        joke.setQuestion(jokeResponseDTO.getQuestion());
        joke.setAnswer(jokeResponseDTO.getAnswer());
        joke.setType(jokeResponseDTO.getType());
        return joke;
    }
}
