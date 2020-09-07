package com.cognizant.arun.lambda;

import java.util.HashMap;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.cognizant.arun.lambda.dynamodb.bean.Customer;
import com.google.gson.Gson;

public class SendEmailHandler {


	static final String FROM = "arunagassi1984@gmail.com";

	// The subject line for the email.
	static final String SUBJECT = "Customer Account Created";

	// The email body for recipients with non-HTML email clients.
	static final String TEXTBODY = "This email was sent through Amazon SES by Arun for Demo " + "using the AWS SDK for Java.";
	
	static final String ENCODING = "UTF-8";

	public void handleRequest(HashMap customerRequest, Context context) {	
		
		context.getLogger().log( "Request is : " + customerRequest);
		
		context.getLogger().log( "Customer request is : " +  (String)(((HashMap)(((HashMap)(((List)(customerRequest.get("Records"))).get(0))).get("Sns"))).get("Message")));
		
		String customerString = (String)((HashMap)(((HashMap)(((List)(customerRequest.get("Records"))).get(0))).get("Sns"))).get("Message");

		Gson gson = new Gson(); 
		Customer customer = gson.fromJson(customerString, Customer.class);

		try {
			
			// The HTML body for the email.
		    String body = "<h1>Account # " + customer.getId() + " Created </h1>";
			
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					.withRegion(Regions.AP_SOUTH_1).build();
			SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(customer.getEmailAddress()))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset(ENCODING).withData(body))
									.withText(new Content().withCharset(ENCODING).withData(TEXTBODY)))
							.withSubject(new Content().withCharset(ENCODING).withData(SUBJECT)))
					.withSource(FROM);
			client.sendEmail(request);
			context.getLogger().log("Email sent!");
		} catch (Exception ex) {
			context.getLogger().log("The email was not sent. Error message: " + ex.getMessage());
		}
	}

}
