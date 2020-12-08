package com.oup.integration.model.jira;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"project",
"reporter",
"timetracking",
"customfield_10005",
"summary",
"description",
"issuetype",
"priority",
"labels",
"components"
})
public class Fields {

@JsonProperty("project")
private Project project;
@JsonProperty("reporter")
private Reporter reporter;
@JsonProperty("timetracking")
private Timetracking timetracking;
@JsonProperty("customfield_10005")
private String customfield10005;
@JsonProperty("summary")
private String summary;
@JsonProperty("description")
private String description;
@JsonProperty("issuetype")
private Issuetype issuetype;
@JsonProperty("priority")
private Priority priority;
@JsonProperty("labels")
private List<String> labels = null;
@JsonProperty("components")
private List<Component> components = null;

@JsonProperty("project")
public Project getProject() {
return project;
}

@JsonProperty("project")
public void setProject(Project project) {
this.project = project;
}

@JsonProperty("reporter")
public Reporter getReporter() {
return reporter;
}

@JsonProperty("reporter")
public void setReporter(Reporter reporter) {
this.reporter = reporter;
}

@JsonProperty("timetracking")
public Timetracking getTimetracking() {
return timetracking;
}

@JsonProperty("timetracking")
public void setTimetracking(Timetracking timetracking) {
this.timetracking = timetracking;
}

@JsonProperty("customfield_10005")
public String getCustomfield10005() {
return customfield10005;
}

@JsonProperty("customfield_10005")
public void setCustomfield10005(String customfield10005) {
this.customfield10005 = customfield10005;
}

@JsonProperty("summary")
public String getSummary() {
return summary;
}

@JsonProperty("summary")
public void setSummary(String summary) {
this.summary = summary;
}

@JsonProperty("description")
public String getDescription() {
return description;
}

@JsonProperty("description")
public void setDescription(String description) {
this.description = description;
}

@JsonProperty("issuetype")
public Issuetype getIssuetype() {
return issuetype;
}

@JsonProperty("issuetype")
public void setIssuetype(Issuetype issuetype) {
this.issuetype = issuetype;
}

@JsonProperty("priority")
public Priority getPriority() {
return priority;
}

@JsonProperty("priority")
public void setPriority(Priority priority) {
this.priority = priority;
}

@JsonProperty("labels")
public List<String> getLabels() {
return labels;
}

@JsonProperty("labels")
public void setLabels(List<String> labels) {
this.labels = labels;
}

@JsonProperty("components")
public List<Component> getComponents() {
return components;
}

@JsonProperty("components")
public void setComponents(List<Component> components) {
this.components = components;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("project", project).append("reporter", reporter).append("timetracking", timetracking).append("customfield10005", customfield10005).append("summary", summary).append("description", description).append("issuetype", issuetype).append("priority", priority).append("labels", labels).append("components", components).toString();
}

}

