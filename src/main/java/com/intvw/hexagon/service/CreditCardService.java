package com.intvw.hexagon.service;

import com.intvw.hexagon.exception.CardNumberGeneratorException;
import com.intvw.hexagon.model.CreditCardAttributes;

public interface CreditCardService {

	String generateCreditCard(CreditCardAttributes cardAttributes) throws CardNumberGeneratorException;
	void validateGeneratedCardNumber(CreditCardAttributes cardAttributes) throws CardNumberGeneratorException;
}
