package com.intvw.hexagon.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.intvw.hexagon.entity.CreditCardEntity;
import com.intvw.hexagon.exception.CardNumberGeneratorException;
import com.intvw.hexagon.model.CreditCardAttributes;
import com.intvw.hexagon.model.CreditCardData;
import com.intvw.hexagon.repository.CreditCardRepository;
import com.intvw.hexagon.service.CreditCardService;
import com.intvw.hexagon.service.impl.CreditCardProprties;

@RestController
@RequestMapping(value="/v1")
public class CreditCardController {

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private CreditCardRepository creditCardRepositoy;
	
	@Autowired
	private CreditCardProprties cardProperties;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(value="/cardtype/{cardType}/numberofcards/{numberOfCards}",method=RequestMethod.GET)
	public ResponseEntity<CreditCardAttributes> generateCreditCardNumber(@PathVariable String cardType,@PathVariable Integer numberOfCards){
		ResponseEntity<CreditCardAttributes> responseData = null;
		
		if(!validCardType(cardType)){
			CardNumberGeneratorException exd = new CardNumberGeneratorException("Card Type is not valid!");
			exd.setStatus(HttpStatus.BAD_REQUEST.value());
			throw exd;
		}
		
		CreditCardAttributes cardAttributes = createCardAttribute(cardType,numberOfCards);
		List<CreditCardData> cardDataList = new ArrayList<>();
		CreditCardData cardData = null;		
		try{
			for(int i=0;i<numberOfCards;i++){
				//Following if condition is to generate equal number of valid and invalid card number so that validation engine can be tested.
				if(i%2==0){
					cardAttributes.setGenerateValidNumber(true);
				}else{
					cardAttributes.setGenerateValidNumber(false);
				}
				String cardNumber = creditCardService.generateCreditCard(cardAttributes);
				cardData = new CreditCardData();
				cardData.setCardNumber(cardNumber);
				cardDataList.add(cardData);
				System.out.println("Generated Card Number = " + cardNumber);
			}
			cardAttributes.setCardDataList(cardDataList);
			
			//Validate generated card number
			creditCardService.validateGeneratedCardNumber(cardAttributes);
			
			//Save valid card into DB
			CreditCardEntity cardEntity = null;
			for(CreditCardData obj : cardAttributes.getCardDataList()){
				cardEntity = new CreditCardEntity();
				cardEntity.setCardExpiryDate(obj.getCardExpiryDate());
				cardEntity.setCardNumber(obj.getCardNumber());
				cardEntity.setCardType(cardAttributes.getCardType());
				creditCardRepositoy.save(cardEntity);
			}
		}catch(Exception ex){
			CardNumberGeneratorException exd = new CardNumberGeneratorException("Card Generation internal server error!" + ex.getMessage());
			exd.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			throw exd;
		}
		responseData = new ResponseEntity<>(cardAttributes,HttpStatus.CREATED);
		return responseData;
	}

	private CreditCardAttributes createCardAttribute(String cardType, Integer numberOfCards) {
		StringBuffer key = new StringBuffer();
		key.append(cardType.toUpperCase());
		String val1 = env.getProperty(key.append(".length").toString());
		System.out.println("val1 == " + val1);
		int len = Integer.parseInt(val1);
		
		key = new StringBuffer();
		key.append(cardType.toUpperCase());
		String val2 = env.getProperty(key.append(".firstdigit").toString());
		
		CreditCardAttributes cardAttributes = new CreditCardAttributes();
		cardAttributes.setLength(len);
		cardAttributes.setStartingDigit(val2);
		cardAttributes.setCardType(cardType);
		System.out.println("cardAttributes = " + cardAttributes);
		return cardAttributes;
	}

	private boolean validCardType(String cardType) {
		boolean validCard = false;
		if(cardProperties != null){
			List<String> supportedVisaTypes = cardProperties.getType();
			for (String ele : supportedVisaTypes) {
				if(cardType.toUpperCase().equals(ele)){
					validCard = true;
					break;
				}
			}
		}
		return validCard;
	}
}
