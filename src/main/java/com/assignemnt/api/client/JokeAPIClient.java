package com.assignemnt.api.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.assignemnt.dto.JokeResponseDTO;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RegisterRestClient(configKey = "joke-api")
@ApplicationScoped
public interface JokeAPIClient {

    @GET
    @Path("/random_joke")
    Uni<JokeResponseDTO> fetchJoke();
}
