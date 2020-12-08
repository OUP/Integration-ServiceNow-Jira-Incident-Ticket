package com.oup.integration.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.RestClientException;
import com.oup.integration.authentication.BasicAuthEncoder;
import com.oup.integration.model.SnowRequest;
import com.oup.integration.model.jira.CreateIssue;
import com.oup.integration.model.jira.transitions.TransitionList;

@Component
public class RouteToUpdateStatusForJIRATicket extends RouteBuilder {

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
		
		
		from("direct:RouteToUpdateStatus")
		.routeId("RouteToUpdateStatusForJIRATicket")
		.log("Updating status for the jira issue ${header.key}")
		//Get all the transitions 
		.setHeader("Authorization", method(basicAuthEncoder, "evaluate"))		
		.setHeader("Content-Type").simple("application/json")
		.setHeader(Exchange.HTTP_METHOD, constant("GET"))
		.toD("{{jira.endpoint}}{{jira.issuePath}}/${header.key}/transitions?proxyAuthScheme=http4&proxyAuthHost=ouparray.oup.com&proxyAuthPort=8080&throwExceptionOnFailure=false")
		.convertBodyTo(String.class)
		.log("Transitions List ${body}")
		.unmarshal().json(JsonLibrary.Jackson, TransitionList.class)		
		.process("ConstructRequestToChangeJIRATicketStatus")
		.marshal().json(JsonLibrary.Jackson.Jackson, CreateIssue.class)
		.log("Request Sent to JIRA ${body}")
		.setHeader("Authorization", method(basicAuthEncoder, "evaluate"))		
		.setHeader("Content-Type").simple("application/json")
		.setHeader(Exchange.HTTP_METHOD, constant("POST"))
		.toD("{{jira.endpoint}}{{jira.issuePath}}/${header.key}/transitions?expand=transitions.fields&proxyAuthScheme=http4&proxyAuthHost=ouparray.oup.com&proxyAuthPort=8080&throwExceptionOnFailure=false");	
	}

}
