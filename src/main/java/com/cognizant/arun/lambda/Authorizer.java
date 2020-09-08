/*
 * Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.cognizant.arun.lambda;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.cognizant.arun.lambda.model.AuthorizerResponse;
import com.cognizant.arun.lambda.model.aws.PolicyDocument;
import com.cognizant.arun.lambda.model.aws.Statement;
import com.cognizant.arun.lambda.util.JwtUtils;

public class Authorizer implements RequestHandler<APIGatewayProxyRequestEvent, AuthorizerResponse> {

	public AuthorizerResponse handleRequest(APIGatewayProxyRequestEvent request, Context context) {
		Map<String, String> headers = request.getHeaders();
		String authorization = headers.get("Authorization");

		context.getLogger().log("authorization " + authorization);

		String jwt = authorization.substring("Bearer ".length());

		String userName = JwtUtils.extractUserName(jwt);

		context.getLogger().log("userName " + userName);

		context.getLogger().log("jwt " + jwt);

		Map<String, String> ctx = new HashMap<>();
		ctx.put("username", userName);

		APIGatewayProxyRequestEvent.ProxyRequestContext proxyContext = request.getRequestContext();
		APIGatewayProxyRequestEvent.RequestIdentity identity = proxyContext.getIdentity();

		String arn = String.format("arn:aws:execute-api:ap-south-1:%s:%s/%s/%s/%s", proxyContext.getAccountId(),
				proxyContext.getApiId(), proxyContext.getStage(), /*proxyContext.getHttpMethod()*/"POST", "*");

		context.getLogger().log("arn " + arn);

		Statement statement = null;
		if ("arunagassi".equalsIgnoreCase(userName)) {
			statement = Statement.builder().resource(arn).effect("Allow").build();
		} else {
			statement = Statement.builder().resource(arn).effect("Deny").build();
		}
		PolicyDocument policyDocument = PolicyDocument.builder().statements(Collections.singletonList(statement))
				.build();
		return AuthorizerResponse.builder().principalId(identity.getAccountId()).policyDocument(policyDocument)
				.context(ctx).build();

	}
}
