package com.intvw.hexagon.service.impl;

import java.util.Date;

import com.intvw.hexagon.model.CreditCardData;

/**
 * Runnable implementation to validate card number.
 * @author pankaj.mahajan
 *
 */
public class CardValidatorThread implements Runnable {

	
	private int cardNumberLength;
	private String startingDigit;
	private CreditCardData cardData;
	
	public CardValidatorThread() {
		super();
	}

	
	public CardValidatorThread(int cardNumberLength, String startingDigit, CreditCardData cardData) {
		super();
		this.cardNumberLength = cardNumberLength;
		this.startingDigit = startingDigit;
		this.cardData = cardData;
	}

	/**
	 * Method checks card validity for length, starting digit and check sum digit.
	 */
	@Override
	public void run() {
		
			//First check length of card number
			this.cardData.setValid(cardData.getCardNumber().length()==this.cardNumberLength);
			
			//Second check starting digit value
			boolean validStartingDigit = false;
			for(int i=0;i<this.startingDigit.length();i++){
				validStartingDigit = this.cardData.getCardNumber().charAt(i)==this.startingDigit.charAt(i);
			}
			this.cardData.setValid(validStartingDigit);
			
			//Third- check card number validity for luhn formula.
			this.cardData.setValid(isValidCardNumber(this.cardData.getCardNumber()));
			
			//Finally - if card number is valid then add expiry date
			if(this.cardData.isValid()){
				this.cardData.setCardExpiryDate(new Date());
			}
	}
	/**
	 * Method checks card validity for last digit.
	 * @param cardNumber
	 * @return
	 */
	private boolean isValidCardNumber(String cardNumber){
		int k=0;
		int multipleOfTwo=0;
		int[] luhnCardNumber = new int[cardNumber.length()];
		for(int i = cardNumber.length()-2;i>=0;i--){
			if(k%2==0){
				multipleOfTwo = Integer.parseInt(String.valueOf(cardNumber.charAt(i))) * 2;
				luhnCardNumber[i] = (multipleOfTwo/10) + (multipleOfTwo%10);
			}else{
				luhnCardNumber[i] = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
			}
			k++;
		}
		int sum = 0;
		for(int i =0; i< luhnCardNumber.length;i++){
			sum += luhnCardNumber[i];
		}
		int checkSumDigit = (sum * 9) % 10; // last valid check sum
		return checkSumDigit == Integer.parseInt(String.valueOf(cardNumber.charAt(cardNumber.length()-1)));
	}
}
