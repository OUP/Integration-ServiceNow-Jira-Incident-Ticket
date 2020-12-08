package com.oup.integration.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.oup.integration.model.SnowRequest;

@Component
public class RouteToAddAttachment extends RouteBuilder{

	@Override
	public void configure() throws Exception {

		from("direct:AddAttachment")
		//.unmarshal().json(JsonLibrary.Jackson, SnowRequest.class)
		.log("Attachment Route started for the SNOW Request ================== ${body}")		
		.process("ProcessorToAddAttachment");
	}

}
