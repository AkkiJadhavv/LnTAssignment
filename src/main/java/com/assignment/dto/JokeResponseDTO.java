package com.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class JokeResponseDTO {  // to return data from the API

	private int id;
	private String question;
	private String answer;
	private String type;
}