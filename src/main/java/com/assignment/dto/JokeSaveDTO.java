package com.assignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class JokeSaveDTO {   // to save data into the database
	private String question;
	private String answer;
	private int id;
}