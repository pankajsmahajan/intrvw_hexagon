package com.intvw.hexagon.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

/**
 * This is rest controller for credit card generator.
 * @author pankaj.mahajan
 *
 */
@RestController
@RequestMapping(value="/v1")
public class CreditCardController {

	private final Logger LOGGER = LoggerFactory.getLogger(CreditCardController.class);
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private CreditCardRepository creditCardRepositoy;
	
	@Autowired
	private CreditCardProprties cardProperties;
	
	@Autowired
	private Environment env;

	/**
	 * This API will take card type and number of cards to be generated.
	 * First it will generate card numbers, then it will validate and if valid then will add 
	 * expire date to card and after that it will save card details into HSQLDB
	 * Finally it will return valid card data.
	 * @param cardType
	 * @param numberOfCards
	 * @return
	 */
	@RequestMapping(value="/cardtype/{cardType}/numberofcards/{numberOfCards}",method=RequestMethod.GET,produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CreditCardAttributes> generateCreditCardNumber(@PathVariable String cardType,@PathVariable Integer numberOfCards){
		LOGGER.info("Method : generateCreditCardNumber - Start");
		
		ResponseEntity<CreditCardAttributes> responseData = null;
		
		if(!validCardType(cardType)){
			throw new CardNumberGeneratorException("Card Type is not valid!");
		}
		if(numberOfCards<=0) {
			throw new CardNumberGeneratorException("Number of cards to be generated should be greator than 0");
		}
		
		CreditCardAttributes cardAttributes = createCardAttribute(cardType);
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
				
			}
			cardAttributes.setCardDataList(cardDataList);
			
			//Validate generated card number
			cardAttributes = creditCardService.validateGeneratedCardNumber(cardAttributes);
			
			//Save valid card into DB
			if(cardAttributes != null) {
				CreditCardEntity cardEntity = null;
				for(CreditCardData obj : cardAttributes.getCardDataList()){
					cardEntity = new CreditCardEntity();
					cardEntity.setCardExpiryDate(obj.getCardExpiryDate());
					cardEntity.setCardNumber(obj.getCardNumber());
					cardEntity.setCardType(cardAttributes.getCardType());
					cardEntity= creditCardRepositoy.save(cardEntity);
					obj.setCardId(cardEntity.getId());
				}
			}
		}catch(Exception ex){
			throw new CardNumberGeneratorException("Card Generation internal server error!" + ex.getMessage());
		}
		responseData = new ResponseEntity<>(cardAttributes,HttpStatus.CREATED);
		LOGGER.info("Method : generateCreditCardNumber - End");
		return responseData;
	}

	/**
	 * Method  to create card attribute object for card number generation service.
	 * @param cardType
	 * @return
	 */
	private CreditCardAttributes createCardAttribute(String cardType) {
		StringBuilder key = new StringBuilder();
		key.append(cardType.toUpperCase());
		String val1 = env.getProperty(key.append(".length").toString());
		int len = Integer.parseInt(val1);
		
		key = new StringBuilder();
		key.append(cardType.toUpperCase());
		String val2 = env.getProperty(key.append(".firstdigit").toString());
		
		CreditCardAttributes cardAttributes = new CreditCardAttributes();
		cardAttributes.setLength(len);
		cardAttributes.setStartingDigit(val2);
		cardAttributes.setCardType(cardType);
		
		return cardAttributes;
	}

	/**
	 * Method to check valid cccard type
	 * @param cardType
	 * @return
	 */
	private boolean validCardType(String cardType) {
		boolean validCard = false;
		if(cardProperties != null){
			List<String> supportedVisaTypes = cardProperties.getType();
			for (String ele : supportedVisaTypes) {
				if(cardType.equalsIgnoreCase(ele)){
					validCard = true;
					break;
				}
			}
		}
		return validCard;
	}
}
