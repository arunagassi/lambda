package com.cognizant.arun.lambda.dynamodb;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.cognizant.arun.lambda.dynamodb.bean.Loan;

public class SaveLoanHandler {

	private DynamoDB dynamoDb;

	private static final String DYNAMO_DB_TABLE_NAME = "Loan";
	private static final Regions REGION = Regions.AP_SOUTH_1;

	public Loan handleRequest(Loan loanRequest, Context context) {
		this.initDynamoDbClient();

		Loan loanResponse = persistData(loanRequest);
		
	    context.getLogger().log("Respoinse: " + loanResponse.toString());

		return loanResponse;
	}

	private Loan persistData(Loan loan) throws ConditionalCheckFailedException {

		UUID uuid = UUID.randomUUID();

		final Map<String, Object> loanMap = new HashMap<String, Object>();
		loanMap.put("loanType", loan.getLoanType());
		loanMap.put("loanAmount", loan.getLoanAmount());
		loanMap.put("durationOfLoan", loan.getDurationOfLoan());
		loanMap.put("rateOfIntrest", loan.getRateOfIntrest());

		dynamoDb.getTable(DYNAMO_DB_TABLE_NAME).putItem(new Item().withPrimaryKey("id", uuid.toString())
				.withString("customer_id", loan.getCustomerId()).withMap("loanMap", loanMap));

		loan.setId(uuid.toString());

		return loan;
	}

	private void initDynamoDbClient() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));
		this.dynamoDb = new DynamoDB(client);
	}

}
