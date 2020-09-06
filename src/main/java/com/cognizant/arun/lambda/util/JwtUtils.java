package com.cognizant.arun.lambda.util;

public final class JwtUtils {

	public static String extractUserName(String jwt) {
		// Dummy method to simulate extracting a username..
		if ("arunagassi".equalsIgnoreCase(jwt)) {
			return "arunagassi";
		} else {
			return null;
		}
	}
}
