package com.oup.integration.service;


import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.jayway.jsonpath.JsonPath;
import com.oup.integration.model.JiraResponse;
import com.oup.integration.model.SnowRequest;


@Controller
public class Router {
		
	@Autowired
	private ProducerTemplate producer;

	@Autowired
	private CamelContext camelContext;

	
	@PostMapping(value = "/incident/ticket",produces = { MediaType.APPLICATION_JSON_VALUE}, consumes= { MediaType.APPLICATION_JSON_VALUE})   
	@ResponseBody
	public ResponseEntity<?> generateResponse(@RequestBody String payload) throws JSONException {

		String openedBy=JsonPath.read(payload, "$.openedBy");
		String username = openedBy.substring(0, openedBy.indexOf("@"));
		
		Exchange exchangeRequestGetAccount = ExchangeBuilder.anExchange(camelContext)
				.withBody(payload)
				.withHeader("username", username)
				.build();
		
		Exchange exchangeResponseGetAccount = producer.send("direct:GetAccountIDFromJIRA", exchangeRequestGetAccount);
		Integer errorCode= exchangeResponseGetAccount.getIn().getHeader("CamelHttpResponseCode", Integer.class);
		String accountId= exchangeResponseGetAccount.getIn().getHeader("AccountId", String.class);
		if(errorCode == 200)
		{
		
		Exchange exchangeRequest = ExchangeBuilder.anExchange(camelContext)
		.withBody(payload)
		.withHeader("AccountId", accountId)
		.build();		
		Exchange exchangeResponse = producer.send("direct:createTicket", exchangeRequest);
		errorCode= exchangeResponse.getIn().getHeader("CamelHttpResponseCode", Integer.class);
		//String status = exchangeResponse.getIn().getHeader("status", String.class);
		if(errorCode == 200 || errorCode == 201 ||  errorCode == 204)
		{
			String jiraResponse = exchangeResponse.getIn().getBody(String.class);			
			return new ResponseEntity<String>(jiraResponse,HttpStatus.OK);
		}
		else
		{
			String jiraResponse = exchangeResponse.getIn().getBody(String.class);					
			return new ResponseEntity<String>(jiraResponse,HttpStatus.valueOf(errorCode));
			
		}
		}
		else
		{
			String jiraResponse = exchangeResponseGetAccount.getIn().getBody(String.class);					
			return new ResponseEntity<String>(jiraResponse,HttpStatus.valueOf(errorCode));
		}
		
		
	}

}
