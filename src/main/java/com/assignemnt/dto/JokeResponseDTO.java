package com.assignemnt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class JokeResponseDTO {  // to return data from the API
	private String id;
	private String question;
	private String answer;
}