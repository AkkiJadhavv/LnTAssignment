package com.assignemnt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class JokeApiResponseDTO {   // to receive responses from the Joke API
    private String id;
    private String setup;
    private String punchline;
}

