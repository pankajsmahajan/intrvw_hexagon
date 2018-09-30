package com.intvw.hexagon.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CardNumberGeneratorExceptionHandler {

	@ExceptionHandler(CardNumberGeneratorException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ExceptionDetails handleCardNumberGeneratorException(CardNumberGeneratorException cngex){
		ExceptionDetails exd = new ExceptionDetails();
		exd.setExceptionMessage(cngex.getMessage());
		exd.setStatus(cngex.getStatus());
		return exd;
	}
}
