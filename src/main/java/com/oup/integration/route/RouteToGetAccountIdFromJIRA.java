package com.oup.integration.route;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.RestClientException;
import com.oup.integration.authentication.BasicAuthEncoder;

@Component("RouteToGetAccountIdFromJIRA")
public class RouteToGetAccountIdFromJIRA extends RouteBuilder {

	@Autowired
	private BasicAuthEncoder basicAuthEncoder;

	
	@Override
	public void configure() throws Exception {
		
		
		onException(Exception.class)
		.handled(true)		
		.setHeader("status").simple("Error")
		.setHeader("errorMessage").simple("Exception occurred in Route ${routeId} . Exception Message: ${exchangeProperty.CamelExceptionCaught}")
		.setBody().simple("${exchangeProperty.CamelExceptionCaught}", String.class)
		.convertBodyTo(String.class)		
		.wireTap("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{header.UniqueId}_$simple{header.RequestReceivedTime}.json")
		.log("Exception occurred in Route ${routeId} . Exception Message: ${exchangeProperty.CamelExceptionCaught}");
		
		onException(RestClientException.class)
		.handled(true)		
		.setHeader("status").simple("Error")
		.setHeader("errorMessage").simple("Exception occurred in Route ${routeId} . Exception Message: ${exchangeProperty.CamelExceptionCaught}")
		.convertBodyTo(String.class)		
		.wireTap("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{header.UniqueId}_$simple{header.RequestReceivedTime}.json")
		.log("${header.errorMessage}");
		
		
		from("direct:GetAccountIDFromJIRA")
		.routeId("GetAccountIDFromJIRA")
		.log("Received Request from SNOW : \n ${body}")
		
		.setHeader("userEmail").simple("${header.username}"+"@oup.com")
		.setHeader("Authorization", method(basicAuthEncoder, "evaluate"))		
		.setHeader("Content-Type").simple("application/json")
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.log("Query Sent to JIRA to get the account Id for user ${header.userEmail}")
		
		.toD("{{jira.endpoint}}{{jira.userPath}}?query=${header.userEmail}&proxyAuthScheme=http4&proxyAuthHost=ouparray.oup.com&proxyAuthPort=8080&throwExceptionOnFailure=false")
		.convertBodyTo(String.class)
		.log("Jira response code:${header.CamelHttpResponseCode} and body: ${body}")
		.choice()
		.when().simple("${header.CamelHttpResponseCode} == 200")
			.choice()
			.when().simple("${body} contains 'accountId'")
				.setHeader("AccountId").jsonpath("$[0].accountId")
				.log("Extracted the account Id ${header.AccountId} for the user ${header.userEmail}")
			.otherwise()
				.log("Response body is empty or accountId not present in response for user ${header.userEmail}")
				.log("Adding the contractor to user ${header.userEmail}")
				.setHeader("userNameWithContractor").simple("${header.username}"+".contractor"+"@oup.com")
				.setHeader("Authorization", method(basicAuthEncoder, "evaluate"))		
				.setHeader("Content-Type").simple("application/json")
				.setHeader(Exchange.HTTP_METHOD, constant("GET"))	
				.log("Query Sent to JIRA to get the account Id for user ${header.userNameWithContractor}")
				.toD("{{jira.endpoint}}{{jira.userPath}}?query=${header.userNameWithContractor}&proxyAuthScheme=http4&proxyAuthHost=ouparray.oup.com&proxyAuthPort=8080&throwExceptionOnFailure=false")
				.convertBodyTo(String.class)
				.log("Jira response code:${header.CamelHttpResponseCode} and body: ${body}")
				.choice()
				.when().simple("${header.CamelHttpResponseCode} == 200 && ${body} contains 'accountId'")
					.setHeader("AccountId").jsonpath("$[0].accountId")
					.log("Extracted the account Id ${header.AccountId} for the user ${header.userNameWithContractor}")
				.otherwise()
					.log("Response body is empty or accountId not present in response for user ${header.userNameWithContractor}")
		.when().simple("${header.CamelHttpResponseCode} == 404")
			.log("User ${header.userEmail} doesnt exist in JIRA")
			.log("Adding the contractor to user ${header.userEmail}")
			.setHeader("userNameWithContractor").simple("${header.username}"+".contractor"+"@oup.com")
			.setHeader("Authorization", method(basicAuthEncoder, "evaluate"))		
			.setHeader("Content-Type").simple("application/json")
			.setHeader(Exchange.HTTP_METHOD, constant("GET"))	
			.log("Query Sent to JIRA to get the account Id for user ${header.userNameWithContractor}")
			.toD("{{jira.endpoint}}{{jira.userPath}}?query=${header.userNameWithContractor}&proxyAuthScheme=http4&proxyAuthHost=ouparray.oup.com&proxyAuthPort=8080&throwExceptionOnFailure=false")
			.convertBodyTo(String.class)
			.choice()
			.when().simple("${header.CamelHttpResponseCode} == 200 && ${body} contains 'accountId'")
				.setHeader("AccountId").jsonpath("$[0].accountId")
				.log("Extracted the account Id ${header.AccountId} for the user ${header.userNameWithContractor}")
		.otherwise()
			.log("Error Response From JIRA : ${body} for the user ${header.userEmail}");
			
		
	}

}
