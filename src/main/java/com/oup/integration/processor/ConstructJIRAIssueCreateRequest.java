package com.oup.integration.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.oup.integration.model.SnowRequest;
import com.oup.integration.model.jira.CreateIssue;
import com.oup.integration.model.jira.Fields;
import com.oup.integration.model.jira.Issuetype;
import com.oup.integration.model.jira.Priority;
import com.oup.integration.model.jira.Project;
import com.oup.integration.model.jira.Reporter;

@Component("ConstructJIRAIssueCreateRequest")
public class ConstructJIRAIssueCreateRequest implements Processor{

	static final String DELIMETER = ",";
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		SnowRequest receivedRequest= exchange.getIn().getBody(SnowRequest.class);
		String accountId= exchange.getIn().getHeader("AccountId",String.class);
		CreateIssue jiraIssue= new CreateIssue();
		Fields fields= new Fields();
		
		//JIRA Board
		Project project= new Project();
		project.setKey(receivedRequest.getJiraboard());
		fields.setProject(project);
		
		//Reporter
		Reporter openedBy= new Reporter();
		openedBy.setAccountId(accountId);
		fields.setReporter(openedBy);
		
		String title = receivedRequest.getTitle();
		
		 if(StringUtils.isNotBlank(title)) {
			 title = title.replaceAll("\\\\r\\\\n|\\\\n", " ");
			 title = title.replaceAll("\"|\\\"|\\\\\"", "\"");
			 title = title.replaceAll("\'|\\'|\\\'|\\\\'", "\'");
		 }
		 
		
		
		fields.setSummary(title);
		
		
		String description = receivedRequest.getDescription();
		//In Jira ticket description '\\' is line break (newline) at Jira end, due to this replaced '\\r' and '\\n' with '\\'.
		if(StringUtils.isNotBlank(description)) {
			description = description.replaceAll("\\\\r\\\\n|\\\\n", " \\\\\\\\ ");
			description = description.replaceAll("\"|\\\"|\\\\\"", "\"");
			description = description.replaceAll("\'|\\'|\\\'|\\\\'", "\'");
		}

		fields.setDescription(description);
		
		Issuetype issueType = new Issuetype();
		issueType.setName("Bug");
		fields.setIssuetype(issueType);
		
		Priority priority = new Priority();		
		priority.setId(Long.toString(receivedRequest.getPriority()));
		fields.setPriority(priority);
		
		
		fields.setLabels(getLabels(receivedRequest.getNumber()));
		
		com.oup.integration.model.jira.Component component= new com.oup.integration.model.jira.Component();
		component.setName(receivedRequest.getDivision());
		List<com.oup.integration.model.jira.Component> componentList= new ArrayList<>();
		componentList.add(component);
		fields.setComponents(componentList);
		
		jiraIssue.setFields(fields);
		
		exchange.getOut().setBody(jiraIssue , CreateIssue.class);
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
	}
	
	
	private List<String> getLabels(String label) {
		
		List<String> labels= new ArrayList<>();
		String[] labelArray = {};
		
		if(label !=null) {
			labelArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(label, DELIMETER);
		}
		labels = Arrays.asList(labelArray);
		return labels;
	}
	
//	public static void main(String[] args) {
//		ConstructJIRAIssueCreateRequest obj = new ConstructJIRAIssueCreateRequest();
//		System.out.println("NULL value list:"+ obj.getLabels(null));
//		System.out.println("Blank value list:"+ obj.getLabels(""));
//		
//		
//		System.out.println("Two Blank values list:"+ obj.getLabels(" "));		
//		System.out.println("Single value list:"+ obj.getLabels("INC000001"));
//		System.out.println("Two values list:"+ obj.getLabels("INC000001,NON-PROD"));
//		
//		System.out.println("Three values list:"+ obj.getLabels("INC000001,NON-PROD,ServiceNow"));
//	}
}
