package com.cognizant.arun.lambda.dynamodb;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent.ProxyRequestContext;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent.RequestIdentity;
import com.cognizant.arun.lambda.Authorizer;
import com.cognizant.arun.lambda.model.AuthorizerResponse;

public class AuthorizerTest {

	@Test
	public void handleRequestTest() {
		Context context = new TestContext();
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Authorization", "Bearer arunagassi");
		request.setHeaders(map);		
		request.setRequestContext(getProxyRequestContext());	
		Authorizer authorizer = new Authorizer();
		AuthorizerResponse response = authorizer.handleRequest(request, context);
		Assert.assertEquals("Allow" , response.getPolicyDocument().Statement.get(0).Effect);
	}
	
	@Test
	public void handleRequestTestDeny() {
		Context context = new TestContext();
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Authorization", "Bearer arun");
		request.setHeaders(map);		
		request.setRequestContext(getProxyRequestContext());	
		Authorizer authorizer = new Authorizer();
		AuthorizerResponse response = authorizer.handleRequest(request, context);
		Assert.assertEquals("Deny" , response.getPolicyDocument().Statement.get(0).Effect);
	}
	
	public ProxyRequestContext getProxyRequestContext(){
		ProxyRequestContext proxyContext = new ProxyRequestContext();
		proxyContext.setAccountId("1234");
		proxyContext.setHttpMethod("GET");
		proxyContext.setApiId("TEST");
		proxyContext.setStage("TEST");
		RequestIdentity identity = new RequestIdentity();
		identity.setAccountId("1234");
		proxyContext.setIdentity(identity);
		return proxyContext;
	}

}
