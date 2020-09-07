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
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.cognizant.arun.lambda.dynamodb.bean.Customer;

public class SaveCustomerHandler {

	private DynamoDB dynamoDb;

	private static final String DYNAMO_DB_TABLE_NAME = "Customer";
	private static final Regions REGION = Regions.AP_SOUTH_1;

	public Customer handleRequest(Customer customerRequest, Context context) {
		this.initDynamoDbClient();

		Customer customerResponse = persistData(customerRequest);
		
		sendMessage(customerResponse, context);

		return customerResponse;
	}

	private Customer persistData(Customer customer) throws ConditionalCheckFailedException {
		
		UUID uuid = UUID.randomUUID();
		final Map<String, Object> customerMap = new HashMap<String, Object>();
		customerMap.put("lastName", customer.getLastName());
		customerMap.put("password", customer.getPassword());
		customerMap.put("emailAddress", customer.getEmailAddress());
		customerMap.put("phoneNumber", customer.getPhoneNumber());
		customerMap.put("street", customer.getAddress().getStreet());
		customerMap.put("city", customer.getAddress().getCity());
		customerMap.put("zip", customer.getAddress().getZipcode());
		
		dynamoDb.getTable(DYNAMO_DB_TABLE_NAME)
				 .putItem(new Item().withPrimaryKey("id", uuid.toString()).withString("first_name", customer.getFirstName()).withMap("customerMap", customerMap));
		
		customer.setId(uuid.toString());
		
		return customer;
	}

	private void initDynamoDbClient() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));
		this.dynamoDb = new DynamoDB(client);
	}
	
	private void sendMessage(Customer customer, Context context){
		
		AmazonSNS snsClient = new AmazonSNSClient();
		snsClient.setRegion(Region.getRegion(REGION));
		String topicArn = "arn:aws:sns:ap-south-1:983255663009:CustomerNotification";
		                     		
		final PublishRequest publishRequest = new PublishRequest(topicArn, customer.toString());
		final PublishResult publishResponse = snsClient.publish(publishRequest);

		// Print the MessageId of the message.
		context.getLogger().log("Message: " + customer.toString());
		context.getLogger().log("MessageId: " + publishResponse.getMessageId());
	}

}
