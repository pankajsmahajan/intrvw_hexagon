package com.intvw.hexagon.model;

import java.io.Serializable;
import java.util.List;

public class CreditCardAttributes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2157557741858189806L;
	private int length;
	private String startingDigit;
	private boolean generateValidNumber;
	private List<CreditCardData> cardDataList;
	private String cardType;
	
	
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public List<CreditCardData> getCardDataList() {
		return cardDataList;
	}
	public void setCardDataList(List<CreditCardData> cardDataList) {
		this.cardDataList = cardDataList;
	}
	public boolean isGenerateValidNumber() {
		return generateValidNumber;
	}
	public void setGenerateValidNumber(boolean generateValidNumber) {
		this.generateValidNumber = generateValidNumber;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	public String getStartingDigit() {
		return startingDigit;
	}
	public void setStartingDigit(String startingDigit) {
		this.startingDigit = startingDigit;
	}
	@Override
	public String toString() {
		return "CreditCardAttributes [length=" + length + ", startingDigit=" + startingDigit
				+  "]";
	}
}
