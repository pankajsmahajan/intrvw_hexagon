package com.intvw.hexagon.exception;

import java.io.Serializable;

/**
 * Custom exception detail class holds data to be send in response to client.
 * @author pankaj.mahajan
 *
 */
public class ExceptionDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1455485002982603177L;
	private int status;
	private String exceptionMessage;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	 
}
