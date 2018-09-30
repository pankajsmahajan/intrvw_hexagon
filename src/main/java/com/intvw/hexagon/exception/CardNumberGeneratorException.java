package com.intvw.hexagon.exception;

public class CardNumberGeneratorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9074587162850121045L;

	private int status;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public CardNumberGeneratorException() {
		super();
	}

	public CardNumberGeneratorException(String message) {
		super(message);
	}
	
	
}
