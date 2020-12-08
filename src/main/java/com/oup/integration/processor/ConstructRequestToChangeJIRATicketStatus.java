package com.oup.integration.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.oup.integration.model.JiraResponse;
import com.oup.integration.model.jira.Transition;
import com.oup.integration.model.jira.Transition_;
import com.oup.integration.model.jira.transitions.TransitionList;

@Component("ConstructRequestToChangeJIRATicketStatus")
public class ConstructRequestToChangeJIRATicketStatus implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {

		TransitionList translitionList=exchange.getIn().getBody(TransitionList.class);
		List<com.oup.integration.model.jira.transitions.Transition> transitionsList = translitionList.getTransitions();
		String transitionName = "To Do";
		//Default to "To Do"
		String transitionId="11";
		for (com.oup.integration.model.jira.transitions.Transition transition : transitionsList) {
			
			if(transition.getName().equalsIgnoreCase(transitionName))
			{
				transitionId=transition.getId();
			}
		}
		Transition transition= new Transition();
		Transition_ transitionStatus= new Transition_();
		// default the status to "To Do" which is of id 21
		transitionStatus.setId(transitionId);
		transition.setTransition(transitionStatus);
		
		exchange.getOut().setBody(transition , Transition.class);
		exchange.getOut().setHeaders(exchange.getIn().getHeaders());
	}

}
