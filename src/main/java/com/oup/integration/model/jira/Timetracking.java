package com.oup.integration.model.jira;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"originalEstimate",
"remainingEstimate"
})
public class Timetracking {

@JsonProperty("originalEstimate")
private String originalEstimate;
@JsonProperty("remainingEstimate")
private String remainingEstimate;

@JsonProperty("originalEstimate")
public String getOriginalEstimate() {
return originalEstimate;
}

@JsonProperty("originalEstimate")
public void setOriginalEstimate(String originalEstimate) {
this.originalEstimate = originalEstimate;
}

@JsonProperty("remainingEstimate")
public String getRemainingEstimate() {
return remainingEstimate;
}

@JsonProperty("remainingEstimate")
public void setRemainingEstimate(String remainingEstimate) {
this.remainingEstimate = remainingEstimate;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("originalEstimate", originalEstimate).append("remainingEstimate", remainingEstimate).toString();
}

}