package com.cognizant.arun.lambda.dynamodb;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.cognizant.arun.lambda.dynamodb.bean.Loan;


public class SaveLoanHandlerTest {

	@Test
	public void handleRequestTest() {
		Context context = new TestContext();
		SaveLoanHandler loanHandler = new SaveLoanHandler();
		Loan loanResponse = loanHandler.handleRequest(getLoan(), context);
		Assert.assertNotNull(loanResponse.getId());
	}
	
	public Loan getLoan(){
		Loan loan = new Loan();
		loan.setCustomerId("1234");
		loan.setDurationOfLoan(120);
		loan.setLoanAmount(1000000);
		loan.setLoanType("MORTGAGETEST");
		loan.setRateOfIntrest("8%");
		return loan;
	}

}
