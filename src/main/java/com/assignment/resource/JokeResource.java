package com.assignment.resource;

//import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.assignment.service.JokeSaveService;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import io.smallrye.mutiny.Uni;

@Slf4j
@Path("/jokes")
//@Tag(name = "Jokes API", description = "API to fetch and store jokes")
public class JokeResource {

    @Inject
    JokeSaveService jokeService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> fetchJokes(@QueryParam("count") int count) {
        log.info("Entering in fetchJokes method from resource class ");
        if (count >= 1 && count <= 100) {
            return jokeService.saveJokes(count)
                    .onItem().transform(jokes -> {
                        if (!jokes.isEmpty()) {
                            log.info("Exiting from fetchJokes method from resource class");
                            return Response.status(Status.OK).entity(jokes).build();
                        } else {
                            log.info("No jokes fetched");
                            return Response.status(Status.NOT_FOUND).entity("No jokes found").build();
                        }
                    })
                    .onFailure().recoverWithItem(th -> {
                        log.error("Exception occurred: {}", th.getMessage());
                        return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to process request").build();
                    });
        } else {
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).entity("Count must be between 1 and 100").build());
        }
    }
}
