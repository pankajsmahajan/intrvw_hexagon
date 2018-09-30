package com.intvw.hexagon.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Utility class to hold card data like; number,type and expiry date.
 * @author pankaj.mahajan
 *
 */
public class CreditCardData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7780171373912722449L;
	
	private String cardNumber;
	private Date cardExpiryDate;
	private boolean valid;
	private long cardId;
	
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Date getCardExpiryDate() {
		return cardExpiryDate;
	}
	public void setCardExpiryDate(Date cardExpiryDate) {
		this.cardExpiryDate = cardExpiryDate;
	}
}
