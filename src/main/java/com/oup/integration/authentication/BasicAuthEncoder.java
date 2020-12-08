package com.oup.integration.authentication;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthEncoder{		

	@Value("${serviceaccount.username}")
	String username;
	
	@Value("${serviceaccount.password}")
	String password;
	
	
	public String evaluate() {
		
		return  "Basic "+ Base64.getEncoder().encodeToString((username+":"+password).getBytes());
	}

}