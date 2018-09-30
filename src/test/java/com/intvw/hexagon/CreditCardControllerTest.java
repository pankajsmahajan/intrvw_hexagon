package com.intvw.hexagon;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.intvw.hexagon.controller.CreditCardController;
import com.intvw.hexagon.entity.CreditCardEntity;
import com.intvw.hexagon.model.CreditCardAttributes;
import com.intvw.hexagon.model.CreditCardData;
import com.intvw.hexagon.repository.CreditCardRepository;
import com.intvw.hexagon.service.impl.CreditCardProprties;
import com.intvw.hexagon.service.impl.CreditCardServiceImpl;

/**
 * Class having jUnit test for rest API
 * @author pankaj.mahajan
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value=CreditCardController.class,secure=false)
public class CreditCardControllerTest {

	private final Logger LOGGER = LoggerFactory.getLogger(CreditCardControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CreditCardServiceImpl cardService;
	
	@MockBean
	private CreditCardProprties cardProperties;
	
	@MockBean
	private CreditCardRepository creditCardRepositoy;
	
	private CreditCardAttributes cardAttributes;
	
	private List<String> visaTypeList;
	
	private CreditCardEntity cardEntity;
	@Before
	public void setUp() {
		this.cardAttributes = new CreditCardAttributes();
		this.cardAttributes.setCardType("VISA");
		this.cardAttributes.setLength(13);
		this.cardAttributes.setStartingDigit("4");
		List<CreditCardData> cardDataList = new ArrayList<>();
		CreditCardData cardData = null;	
		this.cardAttributes.setGenerateValidNumber(true);
		cardData = new CreditCardData();
		cardData.setCardNumber("4576541640491");
		cardDataList.add(cardData);
		this.cardAttributes.setCardDataList(cardDataList);
		this.visaTypeList = new ArrayList<>();
		this.visaTypeList.add("DISCOVER");
		this.visaTypeList.add("AMEX");
		this.visaTypeList.add("MASTER");
		this.visaTypeList.add("VISA");
		this.cardEntity = new CreditCardEntity();
		this.cardEntity.setCardExpiryDate(new Date());
		this.cardEntity.setCardNumber("4576541640491");
		this.cardEntity.setCardType(this.cardAttributes.getCardType());
		this.cardEntity.setId(1);
		
	}
	@Test
	public void testGenerateCreditCardNumber() throws Exception{
		Mockito.when(this.cardService.generateCreditCard(Mockito.any())).thenReturn("4576541640491");
		Mockito.when(this.cardService.validateGeneratedCardNumber(Mockito.any())).thenReturn(this.cardAttributes);
		Mockito.when(this.cardProperties.getType()).thenReturn(this.visaTypeList);
		Mockito.when(this.creditCardRepositoy.save(Mockito.any())).thenReturn(this.cardEntity);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/cardtype/amex/numberofcards/4");
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"length\":13,\"startingDigit\":\"4\",\"generateValidNumber\":true,\"cardDataList\":[{\"cardNumber\":\"4576541640491\",\"cardExpiryDate\":null,\"valid\":false,\"cardId\":1}],\"cardType\":\"VISA\"}";
		
		assertNotNull(result);
		assertNotNull(result.getResponse());
		assertNotNull(result.getResponse().getContentAsString());
		LOGGER.info("JSON response from REST-API " + result.getResponse().getContentAsString());
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
}
