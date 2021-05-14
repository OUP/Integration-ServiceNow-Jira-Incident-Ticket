package com.oup.integration.model.jira.transitions;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"expand",
"transitions"
})
public class TransitionList {

@JsonProperty("expand")
private String expand;
@JsonProperty("transitions")
private List<Transition> transitions = null;

@JsonProperty("expand")
public String getExpand() {
return expand;
}

@JsonProperty("expand")
public void setExpand(String expand) {
this.expand = expand;
}

@JsonProperty("transitions")
public List<Transition> getTransitions() {
	if(transitions == null) {
		transitions = new ArrayList<Transition>();
	}
return transitions;
}

@JsonProperty("transitions")
public void setTransitions(List<Transition> transitions) {
this.transitions = transitions;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("expand", expand).append("transitions", transitions).toString();
}

}