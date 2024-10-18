package com.assignment.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="jokes")
public class Jokes extends PanacheEntity {
		
	@Column(name="id")
    private long id;
	
	@Column(name="question")
    private String question;
	
	@Column(name="answer")
    private String answer;
	
	@Column(name="typee")
    private String type;
    
}