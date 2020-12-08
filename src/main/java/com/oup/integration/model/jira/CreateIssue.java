package com.oup.integration.model.jira;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"fields"
})
public class CreateIssue {

@JsonProperty("fields")
private Fields fields;

@JsonProperty("fields")
public Fields getFields() {
return fields;
}

@JsonProperty("fields")
public void setFields(Fields fields) {
this.fields = fields;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("fields", fields).toString();
}

}

