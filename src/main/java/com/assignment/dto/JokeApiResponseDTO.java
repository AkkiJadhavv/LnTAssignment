package com.assignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JokeApiResponseDTO { // to receive responses from the Joke API
	@JsonProperty("id")
	private int id;
	@JsonProperty("setup")
	private String setup;
	@JsonProperty("punchline")
	private String punchline;
	@JsonProperty("type")
	private String type;
}
