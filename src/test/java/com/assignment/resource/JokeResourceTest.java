package com.assignment.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.assignemnt.dto.JokeSaveDTO;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class JokeResourceTest {

    @Test
    @DisplayName("Should return a list of jokes when valid count is provided")
    public void testGetJokesSuccess() {
        int count = 5;

        given()
            .queryParam("count", count)
        .when()
            .get("/jokes")
        .then()
            .statusCode(200)
            .body("$", hasSize(count))
            .body("[0].question", notNullValue())
            .body("[0].answer", notNullValue());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 0 }) // Invalid counts
    @DisplayName("Should return 400 Bad Request for invalid joke count")
    public void testGetJokesInvalidCount(int count) {
        given()
            .queryParam("count", count)
        .when()
            .get("/jokes")
        .then()
            .statusCode(400)
            .body("message", is("Count must be greater than 0")); // Assuming this is the error message
    }

    @Test
    @DisplayName("Should save a joke successfully")
    public void testSaveJokeSuccess() {
        JokeSaveDTO jokeSaveDTO = new JokeSaveDTO();
        jokeSaveDTO.setQuestion("Why did the chicken cross the road?");
        jokeSaveDTO.setAnswer("To get to the other side.");

        given()
            .contentType("application/json")
            .body(jokeSaveDTO)
        .when()
            .post("/jokes")
        .then()
            .statusCode(200)
            .body("question", equalTo(jokeSaveDTO.getQuestion()))
            .body("answer", equalTo(jokeSaveDTO.getAnswer()));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when saving a joke with invalid data")
    public void testSaveJokeInvalidData() {
        JokeSaveDTO jokeSaveDTO = new JokeSaveDTO();
        jokeSaveDTO.setQuestion(null); // Invalid data

        given()
            .contentType("application/json")
            .body(jokeSaveDTO)
        .when()
            .post("/jokes")
        .then()
            .statusCode(400)
            .body("message", is("Question must not be null")); // Assuming this is the error message
    }
}
