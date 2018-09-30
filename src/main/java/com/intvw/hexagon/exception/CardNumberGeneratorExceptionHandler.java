package com.intvw.hexagon.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Controller advicce to handle different exceptions.
 * @author pankaj.mahajan
 *
 */
@ControllerAdvice
public class CardNumberGeneratorExceptionHandler extends ResponseEntityExceptionHandler{

	/**
	 * Method to handle application specific exception
	 * @param cngex
	 * @return
	 */
	@ExceptionHandler(CardNumberGeneratorException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ExceptionDetails handleCardNumberGeneratorException(CardNumberGeneratorException cngex){
		ExceptionDetails exd = new ExceptionDetails();
		exd.setExceptionMessage(cngex.getMessage());
		return exd;
	}
	
	/**
	 * Method to handle constraint violation exception
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ExceptionDetails handleConstraintVoilationException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
		
		StringBuilder sb = new StringBuilder();
		for(ConstraintViolation<?> violation : violations) {
			sb.append(violation.getMessage() + "\n");
		}
		ExceptionDetails exd = new ExceptionDetails();
		exd.setExceptionMessage(sb.toString());
		return exd;
	}
	
	/**
	 * Method to handle MethodArgumentTypeMismatchException
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ExceptionDetails methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		ExceptionDetails exd = new ExceptionDetails();
		exd.setExceptionMessage(ex.getMessage());
		return exd;
	}
	
	/**
	 * Method to handle MethodArgumentNotValidException
	 */
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve,HttpHeaders headers,HttpStatus status,WebRequest request){
		ExceptionDetails exd = new ExceptionDetails();
		exd.setExceptionMessage(manve.getMessage());
		return handleExceptionInternal(manve, exd, headers, status, request);
	}
	
	
}
