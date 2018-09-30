package com.intvw.hexagon.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.intvw.hexagon.model.CreditCardAttributes;
import com.intvw.hexagon.model.CreditCardData;
import com.intvw.hexagon.service.CreditCardService;

/**
 * Service implementation of credit card service. It provides implementation for generate card method and validate card number.
 * @author pankaj.mahajan
 *
 */
@Service
public class CreditCardServiceImpl implements CreditCardService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(CreditCardServiceImpl.class);
	
	@Override
	public String generateCreditCard(CreditCardAttributes cardAttributes) {
		
		
		LOGGER.info("Method : generateCreditCard - Start");
		//int array for card number
		int[] cardNumber = new int[cardAttributes.getLength()];
		
		//Find starting digit. In case of AMEX it will be 2 digit
		String startingNumber = cardAttributes.getStartingDigit();

		for(int i = 0; i < startingNumber.length();i++){
			cardNumber[i] = Integer.parseInt(String.valueOf(startingNumber.charAt(i)));
		}
		
		/*
		 * start generating random card number from second last digit index.
		 * Run the loop before the position which is already filled with starting digits.
		 */
		int counter = cardAttributes.getLength() - 2;
		
		for(int i = counter; i>=startingNumber.length(); i--){
			cardNumber[i] = ThreadLocalRandom.current().nextInt(0,10);
		}
		
		//Till this point we have cardNumber array filled with starting digit and remaining random numbers excluding last digit.
		//Now we can generate either a valid check sum digit for last position so that we have a valid card number and validation engine 
		//can validate it. we can also generate invalid check sum for last digit so that validation engine can invalidate it.
		
		int[] luhnParseNumber = new int[cardAttributes.getLength()];
		int k=0;
		int multipleOfTwo=0;
		for(int i=cardNumber.length-2;i>=0;i--){
			if(k%2==0){
				multipleOfTwo = cardNumber[i] * 2;
				luhnParseNumber[i] = (multipleOfTwo/10) + (multipleOfTwo%10);
			}else{
				luhnParseNumber[i] = cardNumber[i];
			}
			k++;
		}
		
		if(cardAttributes.isGenerateValidNumber()){
			int sum = 0;
			for(int i =0; i< luhnParseNumber.length;i++){
				sum += luhnParseNumber[i];
			}
			cardNumber[cardAttributes.getLength()-1] = (sum * 9) % 10; // last valid check sum
		}else{
			cardNumber[cardAttributes.getLength()-1] = ThreadLocalRandom.current().nextInt(0,10);
		}
		
		//Convert card number into string value
		StringBuilder creditCardNumber = new StringBuilder();
		for(int i=0; i < cardNumber.length;i++){
			creditCardNumber.append(cardNumber[i]);
		}
		LOGGER.info("Method : generateCreditCard - End");
		return creditCardNumber.toString();
	}

	@Override
	public CreditCardAttributes validateGeneratedCardNumber(CreditCardAttributes cardAttributes) throws InterruptedException {
		CardValidatorThread threadRunnable = null;
		List<Thread> cardValidatorThread = new ArrayList<>();
		for(CreditCardData obj : cardAttributes.getCardDataList()){
			threadRunnable = new CardValidatorThread(cardAttributes.getLength(), cardAttributes.getStartingDigit(), obj);
			Thread validatorThread = new Thread(threadRunnable);
			cardValidatorThread.add(validatorThread);
			validatorThread.start();
		}
		
			for(Thread obj : cardValidatorThread){
				obj.join();
			}
		
		List<CreditCardData> validCardList = new ArrayList<>();
		for(CreditCardData obj : cardAttributes.getCardDataList()){
			if(obj.getCardExpiryDate()!=null && obj.isValid()){
				validCardList.add(obj);
			}
		}
		cardAttributes.setCardDataList(validCardList);
		return cardAttributes;
	}


}
