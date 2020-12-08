package com.oup.integration.processor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.input.AttachmentInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.oup.integration.model.SNOWAttachment;
import com.oup.integration.model.SnowRequest;

@Component("ProcessorToAddAttachment")
public class ProcessorToAddAttachment implements Processor{

	@Value("${serviceaccount.username}") String username;
	@Value("${serviceaccount.password}") String password;
	@Value("${jira.attachment.endpoint}") String endpoint;
	@Value("${jira.issuePath}") String issuePath;
	
	@Override
	public void process(Exchange exchange) throws Exception {

		SnowRequest requestBody = exchange.getIn().getBody(SnowRequest.class);
		String jiraKey= exchange.getIn().getHeader("key", String.class);
		JiraRestClient restClient = null;
		
		URI jiraUri = new URI(endpoint);
		
		System.getProperties().put("https.proxyHost", "ouparray.oup.com");
		System.getProperties().put("https.proxyPort", "8080");
		restClient = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(jiraUri,username, password);
		final IssueRestClient client = restClient.getIssueClient();
		List<SNOWAttachment> attachmentList = requestBody.getAttachments();
		URI attachmentsUri = new URI(endpoint + issuePath + jiraKey+"/attachments");
		for(SNOWAttachment attachment : attachmentList)			
		{			 
			byte[] decodedBytes = Base64.getDecoder().decode(attachment.getAttachmentId());	            
			InputStream is = new ByteArrayInputStream(decodedBytes);
			AttachmentInput jiraAttachment=new AttachmentInput(attachment.getAttachmentName(), is);
			client.addAttachments(attachmentsUri, jiraAttachment).claim();
		}
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
		
	}

}
