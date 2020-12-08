package com.oup.integration.model.jira;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"accountId"
})
public class Reporter {

@JsonProperty("accountId")
private String accountId;

@JsonProperty("accountId")
public String getAccountId() {
return accountId;
}

@JsonProperty("accountId")
public void setAccountId(String accountId) {
this.accountId = accountId;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("accountId", accountId).toString();
}

}

