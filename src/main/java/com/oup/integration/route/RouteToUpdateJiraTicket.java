package com.oup.integration.route;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.RestClientException;
import com.oup.integration.authentication.BasicAuthEncoder;
import com.oup.integration.model.jira.CreateIssue;

@Component
public class RouteToUpdateJiraTicket extends RouteBuilder {
	
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
		
		/*
		 * Update Issue data: 
		 * Add a Comment: POST /rest/api/2/issue/{issueIdOrKey}/comment data={   "body":"..." ... }
		 * 
    }
}
		 */
		from("direct:UpdateJIRATicket")
		.routeId("RouteToUpdateJIRATicket")
		.log("Creating a Request to update JIRA Ticket ${header.Ticket} for Incident: ${header.Number}")
		.process("ConstructJIRAIssueCreateRequest")
		.marshal().json(JsonLibrary.Jackson.Jackson, CreateIssue.class)
		.log("Request Sent to JIRA ${body}")
		.setHeader("Authorization", method(basicAuthEncoder, "evaluate"))		
		.setHeader("Content-Type").simple("application/json;charset=UTF-8")
		.setHeader(Exchange.HTTP_METHOD, constant("PUT"))
		.toD("{{jira.endpoint}}{{jira.issuePath}}${header.Ticket}?throwExceptionOnFailure=false{{jira.proxy}}");
		
	}

}
