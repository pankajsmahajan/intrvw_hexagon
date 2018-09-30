package com.intvw.hexagon;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.intvw.hexagon.entity.CreditCardEntity;
import com.intvw.hexagon.model.CreditCardAttributes;
import com.intvw.hexagon.model.CreditCardData;
import com.intvw.hexagon.repository.CreditCardRepository;
import com.intvw.hexagon.service.CreditCardService;

/**
 * Class has jUnit test methods for methods of credit-card-service.
 * @author HP
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InterviewHexagonApplicationTests {

	private final Logger LOGGER = LoggerFactory.getLogger(InterviewHexagonApplicationTests.class);
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private CreditCardRepository creditCardRepositoy;
	
	private CreditCardAttributes cardAttributes;
	
	@Before
	public void setUp() {
		this.cardAttributes = new CreditCardAttributes();
		this.cardAttributes.setCardType("VISA");
		this.cardAttributes.setLength(13);
		this.cardAttributes.setStartingDigit("4");
		
		List<CreditCardData> cardDataList = new ArrayList<>();
		CreditCardData cardData = null;
		cardData = new CreditCardData();
		cardData.setCardNumber("4517293929849");
		cardDataList.add(cardData);
		
		cardData = new CreditCardData();
		cardData.setCardNumber("4025837573602");
		cardDataList.add(cardData);
		
		cardData = new CreditCardData();
		cardData.setCardNumber("4101221993027");
		cardDataList.add(cardData);
		
		this.cardAttributes.setCardDataList(cardDataList);
	}
	@Test
	public void testGenerateCard() {
		for(int i=0;i<4;i++){
			//Following if condition is to generate equal number of valid and invalid card number so that validation engine can be tested.
			if(i%2==0){
				this.cardAttributes.setGenerateValidNumber(true);
			}else{
				this.cardAttributes.setGenerateValidNumber(false);
			}
			String cardNumber = this.creditCardService.generateCreditCard(this.cardAttributes);
			assertNotNull(cardNumber);
			LOGGER.info("Generated card number = " + cardNumber);
		}
	}

	@Test
	public void testValidateCardNumber() {
		try {
			CreditCardAttributes localObj = this.creditCardService.validateGeneratedCardNumber(this.cardAttributes);
			assertNotNull(localObj);
			assertNotNull(localObj.getCardDataList());
			assertNotNull(localObj.getCardDataList().get(0));
			assertNotNull(localObj.getCardDataList().get(0).getCardExpiryDate());
			LOGGER.info("Expiry date of a valid card number = " + localObj.getCardDataList().get(0).getCardExpiryDate());
		}catch(InterruptedException ixe) {
		}
	}
	
	@Test
	public void testSaveCardEntity() {
		//Save valid card into DB
		CreditCardEntity cardEntity = null;
		for(CreditCardData obj : this.cardAttributes.getCardDataList()){
			cardEntity = new CreditCardEntity();
			cardEntity.setCardExpiryDate(obj.getCardExpiryDate());
			cardEntity.setCardNumber(obj.getCardNumber());
			cardEntity.setCardType(this.cardAttributes.getCardType());
			cardEntity = this.creditCardRepositoy.save(cardEntity);
			assertNotNull(cardEntity);
			assertNotNull(cardEntity.getId());
			LOGGER.info("ID of card entity saved in DB = " + cardEntity.getId());
		}
	}
}
