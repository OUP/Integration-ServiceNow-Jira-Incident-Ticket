package com.oup.integration.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.RestClientException;
import com.oup.integration.model.JiraResponse;
import com.oup.integration.model.SnowRequest;
import java.util.UUID;
@Component
public class MainRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		onException(Exception.class)
		.handled(true)		
		.setHeader("status").simple("Error")
		.setHeader("CamelHttpResponseCode").simple("500")
		.setHeader("errorMessage").simple("Exception occurred in Route ${routeId} . Exception Message: ${exchangeProperty.CamelExceptionCaught}")
		.setBody().simple("${exchangeProperty.CamelExceptionCaught}", String.class)
		.convertBodyTo(String.class)		
		.wireTap("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{header.UniqueId}_$simple{header.RequestReceivedTime}.json")
		.log("Exception occurred in Route ${routeId} . Exception Message: ${exchangeProperty.CamelExceptionCaught}");
		
		onException(RestClientException.class)
		.handled(true)		
		.setHeader("status").simple("Error")
		.setHeader("CamelHttpResponseCode").simple("${header.errorCode}")
		.setHeader("errorMessage").simple("Exception occurred in Route ${routeId} . Exception Message: ${exchangeProperty.CamelExceptionCaught}")
		.setBody().simple("${header.ExceptionMessage}", String.class)
		.convertBodyTo(String.class)		
		.wireTap("file:{{file.backup.location}}/Error?fileName=${date:now:yyyy/MM/dd/}$simple{header.UniqueId}_$simple{header.RequestReceivedTime}.json")
		.log("${header.errorMessage}");
		
		
		
		from("direct:createTicket")
		.routeId("RouteToCreateTicket")
		.log("Received Request from SNOW ================== ${body}")		
        .setHeader("UniqueId",simple(UUID.randomUUID().toString()))
		.setHeader("RequestReceivedTime", simple("${date:now:HHmmssSSS}"))
		.setHeader("Title").jsonpath("$.title")
		.setHeader("Number").jsonpath("$.number")
		.wireTap("file:{{file.backup.location}}/1.0 SNOWRequest?fileName=${date:now:yyyy/MM/dd/}$simple{header.UniqueId}_$simple{header.RequestReceivedTime}.json")
		.log("Backup of the SNOW request successful for the Incident Ticket title: $simple{headerTitle} : $simple{header.UniqueId}_$simple{header.RequestReceivedTime}.json")
		.unmarshal().json(JsonLibrary.Jackson, SnowRequest.class)
		.setProperty("SnowRequest").simple("${body}")
		.log("unmarshall succesfull for the SNOW Request ================== ${body}")		
		.to("direct:CreateJIRATicket")
		.convertBodyTo(String.class)
		.choice()
			.when().simple("${header.CamelHttpResponseCode} == 201")				
				.log("Succesfully created the ticket in JIRA : ${body}")
				.convertBodyTo(String.class)
				.toD("file:{{file.backup.location}}/2.0 JIRAResponse?fileName=${date:now:yyyy/MM/dd/}$simple{header.UniqueId}_$simple{header.RequestReceivedTime}.json")
				.convertBodyTo(String.class)				
				.setHeader("key").jsonpath("$.key")
				.setHeader("self").jsonpath("$.self")
				.setHeader("JIRAResponse").simple("${body}")
				.setBody().simple("${exchangeProperty.SnowRequest}", SnowRequest.class)
				.to("direct:AddAttachment")
				.log("Attachments have been succesfully added")
				.log("Status Update started")
				.to("direct:RouteToUpdateStatus")				
				.log("Body of message after processing ================== ${body}")
				.setBody().simple("${header.JIRAResponse}")
				.log("Response Sent to SNOW ================== ${body}")
			.otherwise()
				.log("Error occurred when creating a issue in JIRA : ${body}");
		
		from("direct:updateTicket")
		.routeId("RouteToUpdateTicket")
		.log("Received Request from SNOW ================== ${body}")		
        .setHeader("UniqueId",simple(UUID.randomUUID().toString()))
		.setHeader("RequestReceivedTime", simple("${date:now:HHmmssSSS}"))
		.setHeader("Title").jsonpath("$.title")
		.setHeader("Number").jsonpath("$.number")
		.wireTap("file:{{file.backup.location}}/1.0 SNOWRequest?fileName=${date:now:yyyy/MM/dd/}$simple{header.UniqueId}_$simple{header.RequestReceivedTime}.json")
		.log("Backup of the SNOW request successful for the Incident Ticket title: $simple{headerTitle} : $simple{header.UniqueId}_$simple{header.RequestReceivedTime}.json")
		.unmarshal().json(JsonLibrary.Jackson, SnowRequest.class)
		.setProperty("SnowRequest").simple("${body}")
		.log("unmarshall succesfull for the SNOW Request ================== ${body}")		
		.to("direct:UpdateJIRATicket")
		.convertBodyTo(String.class)
		.choice()
			.when().simple("${header.CamelHttpResponseCode} == 204")				
				.log("Succesfully updated the ticket in JIRA : ${body}")
				.convertBodyTo(String.class)				
				.setHeader("key").jsonpath("$.key")
				.setHeader("self").jsonpath("$.self")
				.setHeader("JIRAResponse").simple("${body}")
//				.setBody().simple("${exchangeProperty.SnowRequest}", SnowRequest.class)
//				.to("direct:AddAttachment")
//				.log("Attachments have been succesfully added")
				.log("Status Update started")
				.to("direct:RouteToUpdateStatus")				
				.log("Body of message after processing ================== ${body}")
				.setBody().simple("${header.JIRAResponse}")
				.log("Response Sent to SNOW ================== ${body}")
			.otherwise()
				.log("Error occurred when creating a issue in JIRA : ${body}");
	}

}
