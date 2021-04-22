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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.jayway.jsonpath.DocumentContext;
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
	static String NOTSET="NotSet";
	@PostMapping(value = "/update/{ticketId}",produces = { MediaType.APPLICATION_JSON_VALUE}, consumes= { MediaType.APPLICATION_JSON_VALUE})   
	@ResponseBody
	public ResponseEntity<?> updateResponse(@PathVariable String ticketId, @RequestBody String payload) throws JSONException {

		DocumentContext Jpayload = JsonPath.parse(payload);
		String openedBy=Jpayload.read( "$.openedBy");
		String username = openedBy.substring(0, openedBy.indexOf("@"));
		String Status = NOTSET;
		try {
			Status = Jpayload.read("$.status"); // may fault if key doesn't exist
			Jpayload.delete("$.status");
			// may need to translate from SNOW to JIRA status
		}catch(Exception e) {
			// ignore
		}
		Exchange exchangeRequestGetAccount = ExchangeBuilder.anExchange(camelContext)
				.withBody(Jpayload.jsonString())
				.withHeader("username", username)
				.build();
		
		Exchange exchangeResponseGetAccount = producer.send("direct:GetAccountIDFromJIRA", exchangeRequestGetAccount);
		Integer errorCode= exchangeResponseGetAccount.getIn().getHeader("CamelHttpResponseCode", Integer.class);
		String accountId= exchangeResponseGetAccount.getIn().getHeader("AccountId", String.class);
		if(errorCode == 200)
		{
			if(Status != NOTSET) {
			// Transition ticket to new status
				Exchange WorkflowRequest = ExchangeBuilder.anExchange(camelContext)
				.withBody(Status)
				.withHeader("AccountId", accountId)
				.withHeader("Ticket", ticketId)
				.build();		
				Exchange WorkflowResponse = producer.send("direct:workflowTicket", WorkflowRequest);
				errorCode= WorkflowResponse.getIn().getHeader("CamelHttpResponseCode", Integer.class);
			
				if(errorCode > 299 )
				{
					String jiraResponse = WorkflowResponse.getIn().getBody(String.class);			
					return new ResponseEntity<String>(jiraResponse,HttpStatus.OK);					
				}
			}
		
			// Update ticket's other fields
			Exchange exchangeRequest = ExchangeBuilder.anExchange(camelContext)
			.withBody(payload)
			.withHeader("AccountId", accountId)
			.withHeader("Ticket", ticketId)
			.build();		
			Exchange exchangeResponse = producer.send("direct:updateTicket", exchangeRequest);
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
