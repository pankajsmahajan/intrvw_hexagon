package com.intvw.hexagon.service;

import com.intvw.hexagon.model.CreditCardAttributes;

/**
 * Service interface having card generation-validation methods
 * @author pankaj.mahajan
 *
 */
public interface CreditCardService {

	/**
	 * Method generate card number. Currently it generates valid and invalid card numbers.
	 * Valid numbers can be validated by validation method.
	 * @param cardAttributes
	 * @return
	 */
	String generateCreditCard(CreditCardAttributes cardAttributes);
	/**
	 * This method checks validity of card numbers for a given card type.
	 * It will start a new thread for each card number.
	 * @param cardAttributes
	 * @return
	 * @throws InterruptedException
	 */
	CreditCardAttributes validateGeneratedCardNumber(CreditCardAttributes cardAttributes) throws InterruptedException;
}
