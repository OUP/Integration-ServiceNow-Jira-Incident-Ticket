package com.oup.integration.route;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.RestClientException;
import com.oup.integration.authentication.BasicAuthEncoder;
import com.oup.integration.model.jira.CreateIssue;
import com.oup.integration.model.jira.transitions.TransitionList;

@Component
public class transitionTicket extends RouteBuilder {

	
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
		 * Update Issue workflow status: 
		 * : POST /rest/api/2/issue/{issueIdOrKey}/transition data={   "id":"..." ... }
		 * 
		 */
		from("direct:UpdateWorkFlowTicket")
		.routeId("RouteToUpdateJIRATicket")
		.log("Request to transition JIRA Ticket ${header.Ticket} for Incident: ${header.Number} to step ${header.Status}")
		.process(new Processor() {public void process(Exchange exchange) throws Exception {
			String Step = "{\"transition\":{\"id\":\""+(String) exchange.getIn().getHeader("toWorkflowID")+"\"}}";
			exchange.getIn().setBody(Step);
		} })
		.log("Request Sent to JIRA ${body}")
		.setHeader("Authorization", method(basicAuthEncoder, "evaluate"))		
		.setHeader("Content-Type").simple("application/json;charset=UTF-8")
		.setHeader(Exchange.HTTP_METHOD, constant("POST"))
		.toD("{{jira.endpoint}}{{jira.issuePath}}${header.Ticket}/transition?throwExceptionOnFailure=false{{jira.proxy}}");

		/*
		 * Get possible workflow items from Jira (need to convert the "step name" into the target workflow ID
		 * for the above update
		 * 
		 */
		from("direct:GetTicketWorkflow")
		 .routeId("RouteToGetWorkflow")
		 .log("Get workflow for  ${header.Ticket}")
		 .setHeader("Authorization", method(basicAuthEncoder, "evaluate"))
		 .setHeader(Exchange.HTTP_METHOD, constant("GET"))
		 .toD("{{jira.endpoint}}{{jira.issuePath}}${header.Ticket}/transitions?throwExceptionOnFailure=false")
		 //.convertBodyTo(String.class)
		 .convertBodyTo(TransitionList.class)
			.process(new Processor() {public void process(Exchange exchange) throws Exception {
				@SuppressWarnings("unused")
				String body = (String) exchange.getIn().getBody(String.class);
			} });

	}

}
