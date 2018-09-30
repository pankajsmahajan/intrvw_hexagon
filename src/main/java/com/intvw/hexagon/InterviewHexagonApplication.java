package com.intvw.hexagon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.intvw.hexagon.service.impl.CreditCardProprties;

@SpringBootApplication
public class InterviewHexagonApplication {

	@Autowired
	CreditCardProprties cardProperties;
	
	public static void main(String[] args) {
		SpringApplication.run(InterviewHexagonApplication.class, args);
	}
}
