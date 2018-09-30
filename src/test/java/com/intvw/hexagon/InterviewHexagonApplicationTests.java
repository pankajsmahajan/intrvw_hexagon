package com.intvw.hexagon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.intvw.hexagon.entity.CreditCardEntity;
import com.intvw.hexagon.model.CreditCardAttributes;
import com.intvw.hexagon.model.CreditCardData;
import com.intvw.hexagon.repository.CreditCardRepository;
import com.intvw.hexagon.service.CreditCardService;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterviewHexagonApplicationTests {

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private CreditCardRepository creditCardRepositoy;
	
	@Test
	public void testGenerateCard() {
		CreditCardAttributes cardAttributes = new CreditCardAttributes();
		cardAttributes.setCardType("VISA");
		cardAttributes.setLength(13);
		cardAttributes.setStartingDigit("4");
		List<CreditCardData> cardDataList = new ArrayList<>();
		CreditCardData cardData = null;	
		for(int i=0;i<4;i++){
			//Following if condition is to generate equal number of valid and invalid card number so that validation engine can be tested.
			if(i%2==0){
				cardAttributes.setGenerateValidNumber(true);
			}else{
				cardAttributes.setGenerateValidNumber(false);
			}
			String cardNumber = creditCardService.generateCreditCard(cardAttributes);
			System.out.println("card number === " + cardNumber);
			assertNotNull(cardNumber);
			cardData = new CreditCardData();
			cardData.setCardNumber(cardNumber);
			cardDataList.add(cardData);
			System.out.println("Generated Card Number = " + cardNumber);
		}
		cardAttributes.setCardDataList(cardDataList);
		creditCardService.validateGeneratedCardNumber(cardAttributes);
		for(CreditCardData obj : cardAttributes.getCardDataList()){
			System.out.println("Validated card number " + obj.getCardNumber());
			System.out.println("Validated Card expiry date " + obj.getCardExpiryDate());
			
		}
		
		//Save valid card into DB
		CreditCardEntity cardEntity = null;
		for(CreditCardData obj : cardAttributes.getCardDataList()){
			cardEntity = new CreditCardEntity();
			cardEntity.setCardExpiryDate(obj.getCardExpiryDate());
			cardEntity.setCardNumber(obj.getCardNumber());
			cardEntity.setCardType(cardAttributes.getCardType());
			creditCardRepositoy.save(cardEntity);
		}
		for(CreditCardEntity obj : creditCardRepositoy.findAll()){
			System.out.println("Saved card number " + obj.getCardNumber());
			System.out.println("Saved card type " + obj.getCardType());
			System.out.println("Saved card ID " + obj.getId());
			System.out.println("Saved card expiry date " + obj.getCardExpiryDate());
		}
	}

}
