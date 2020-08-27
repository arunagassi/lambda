package com.cognizant.arun.lambda.dynamodb.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Loan {
	
	private String id;
	private String loanType;
	private int loanAmount;
	private int durationOfLoan;
	private String rateOfIntrest;
	private String customerId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public int getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(int loanAmount) {
		this.loanAmount = loanAmount;
	}
	public int getDurationOfLoan() {
		return durationOfLoan;
	}
	public void setDurationOfLoan(int durationOfLoan) {
		this.durationOfLoan = durationOfLoan;
	}
	public String getRateOfIntrest() {
		return rateOfIntrest;
	}
	public void setRateOfIntrest(String rateOfIntrest) {
		this.rateOfIntrest = rateOfIntrest;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String toString() {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

}
