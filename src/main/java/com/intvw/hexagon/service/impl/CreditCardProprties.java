package com.intvw.hexagon.service.impl;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class is mapped to external properties of a credit card like; type of card, card number length and starting digit of card.
 * Currently properties are configured for VISA, AMEX,MASTER,DISCOVER.
 * If system needs to support additional new card type then corresponding properties can be configured in application.properties file.
 * @author pankaj.mahajan
 *
 */
@Configuration
@ConfigurationProperties("creditcard")
public class CreditCardProprties {

	private int supportedtypes;
	private List<Integer> length;
	private List<String> type;
	private List<Integer> firstdigit;
	
	public int getSupportedtypes() {
		return supportedtypes;
	}
	public void setSupportedtypes(int supportedtypes) {
		this.supportedtypes = supportedtypes;
	}
	public List<Integer> getLength() {
		return length;
	}
	public void setLength(List<Integer> length) {
		this.length = length;
	}
	public List<String> getType() {
		return type;
	}
	public void setType(List<String> type) {
		this.type = type;
	}
	public List<Integer> getFirstdigit() {
		return firstdigit;
	}
	public void setFirstdigit(List<Integer> firstdigit) {
		this.firstdigit = firstdigit;
	}
		
}
