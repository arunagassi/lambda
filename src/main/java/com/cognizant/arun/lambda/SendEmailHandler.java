package com.cognizant.arun.lambda;

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

public class SendEmailHandler {


	static final String FROM = "arunagassi1984@gmail.com";

	// The subject line for the email.
	static final String SUBJECT = "Customer Account Created";

	// The email body for recipients with non-HTML email clients.
	static final String TEXTBODY = "This email was sent through Amazon SES by Arun for Demo " + "using the AWS SDK for Java.";

	public void handleRequest(Customer customerRequest, Context context) {

		try {
			
			// The HTML body for the email.
		    String body = "<h1>Account # " + customerRequest.getId() + " Created </h1>";
			
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					.withRegion(Regions.AP_SOUTH_1).build();
			SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(customerRequest.getEmailAddress()))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(body))
									.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
							.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
					.withSource(FROM);
			client.sendEmail(request);
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent. Error message: " + ex.getMessage());
		}
	}

}