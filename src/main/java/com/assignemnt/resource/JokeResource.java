package com.assignemnt.resource;


import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import com.assignemnt.dto.JokeResponseDTO;
import com.assignemnt.dto.JokeSaveDTO;
import com.assignemnt.service.JokeService;

import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/jokes")
@Tag(name = "Jokes API", description = "API to fetch and store jokes")
public class JokeResource {

	@Inject
    JokeService jokeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Single<Response> getJokes(@QueryParam("count") int count) {
    	if (count>=1 && count<=100) {
        return jokeService.getJokes(count)
            .map(jokes -> Response.ok(jokes).build())
            .onErrorReturn(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build());
    }
    	return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<Response> saveJoke(JokeSaveDTO jokeSaveDTO) {
        return jokeService.saveJoke(jokeSaveDTO)
            .map(jokeResponse -> Response.ok(jokeResponse).build())
            .onErrorReturn(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build());
    }
}
