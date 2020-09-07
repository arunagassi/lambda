package com.cognizant.arun.lambda.dynamodb;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.cognizant.arun.lambda.dynamodb.bean.Address;
import com.cognizant.arun.lambda.dynamodb.bean.Customer;

public class SaveCustomerHandlerTest {

	@Test
	public void handleRequestTest() {
		Context context = new TestContext();
		SaveCustomerHandler customerHandler = new SaveCustomerHandler();
		Customer customerResponse = customerHandler.handleRequest(getCustomer(), context);
		Assert.assertNotNull(customerResponse.getId());
	}
	
	public Customer getCustomer(){
		
		Customer customer = new Customer();
		customer.setFirstName("test");
		customer.setLastName("test");
		customer.setEmailAddress("test@xyz.com");
		customer.setPassword("password");
		customer.setPhoneNumber("123456789");
		customer.setAddress(getAddress());
		return customer;
	}
	
	public Address getAddress(){
		Address address = new Address();
		address.setCity("TestCity");
		address.setStreet("Test Street");
		address.setZipcode("1234");
		return address;		
	}
}
