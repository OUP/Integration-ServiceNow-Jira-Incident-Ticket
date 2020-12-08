

package com.oup.integration.model.jira;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"transition"
})
public class Transition {

@JsonProperty("transition")
private Transition_ transition;

@JsonProperty("transition")
public Transition_ getTransition() {
return transition;
}

@JsonProperty("transition")
public void setTransition(Transition_ transition) {
this.transition = transition;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("transition", transition).toString();
}

}


