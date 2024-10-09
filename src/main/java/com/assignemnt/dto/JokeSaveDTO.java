package com.assignemnt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class JokeSaveDTO {   // to save data into the database
	private String question;
	private String answer;
}