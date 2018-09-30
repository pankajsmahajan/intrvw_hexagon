package com.intvw.hexagon.exception;

/**
 * Application specific exception class.
 * @author pankaj.mahajan
 *
 */
public class CardNumberGeneratorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9074587162850121045L;

	public CardNumberGeneratorException() {
		super();
	}

	public CardNumberGeneratorException(String message) {
		super(message);
	}
	
	
}
