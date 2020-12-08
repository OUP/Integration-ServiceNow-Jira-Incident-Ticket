package com.oup.integration.model.jira.transitions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"name",
"to",
"hasScreen",
"isGlobal",
"isInitial",
"isAvailable",
"isConditional"
})
public class Transition {

@JsonProperty("id")
private String id;
@JsonProperty("name")
private String name;
@JsonProperty("to")
private To to;
@JsonProperty("hasScreen")
private Boolean hasScreen;
@JsonProperty("isGlobal")
private Boolean isGlobal;
@JsonProperty("isInitial")
private Boolean isInitial;
@JsonProperty("isAvailable")
private Boolean isAvailable;
@JsonProperty("isConditional")
private Boolean isConditional;

@JsonProperty("id")
public String getId() {
return id;
}

@JsonProperty("id")
public void setId(String id) {
this.id = id;
}

@JsonProperty("name")
public String getName() {
return name;
}

@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

@JsonProperty("to")
public To getTo() {
return to;
}

@JsonProperty("to")
public void setTo(To to) {
this.to = to;
}

@JsonProperty("hasScreen")
public Boolean getHasScreen() {
return hasScreen;
}

@JsonProperty("hasScreen")
public void setHasScreen(Boolean hasScreen) {
this.hasScreen = hasScreen;
}

@JsonProperty("isGlobal")
public Boolean getIsGlobal() {
return isGlobal;
}

@JsonProperty("isGlobal")
public void setIsGlobal(Boolean isGlobal) {
this.isGlobal = isGlobal;
}

@JsonProperty("isInitial")
public Boolean getIsInitial() {
return isInitial;
}

@JsonProperty("isInitial")
public void setIsInitial(Boolean isInitial) {
this.isInitial = isInitial;
}

@JsonProperty("isAvailable")
public Boolean getIsAvailable() {
return isAvailable;
}

@JsonProperty("isAvailable")
public void setIsAvailable(Boolean isAvailable) {
this.isAvailable = isAvailable;
}

@JsonProperty("isConditional")
public Boolean getIsConditional() {
return isConditional;
}

@JsonProperty("isConditional")
public void setIsConditional(Boolean isConditional) {
this.isConditional = isConditional;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("id", id).append("name", name).append("to", to).append("hasScreen", hasScreen).append("isGlobal", isGlobal).append("isInitial", isInitial).append("isAvailable", isAvailable).append("isConditional", isConditional).toString();
}

}

