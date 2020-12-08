package com.oup.integration.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "title", "openedBy", "division", "description", "priority", "number", "jiraBoard", "attachments" })
public class SnowRequest {

	@JsonProperty("title")
	private String title;
	@JsonProperty("openedBy")
	private String openedBy;
	@JsonProperty("division")
	private String division;
	@JsonProperty("description")
	private String description;
	@JsonProperty("priority")
	private Long priority;
	@JsonProperty("number")
	private String number;
	@JsonProperty("jiraboard")
	private String jiraboard;
	@JsonProperty("attachments")
	private List<SNOWAttachment> attachments = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("openedBy")
	public String getOpenedBy() {
		return openedBy;
	}

	@JsonProperty("openedBy")
	public void setOpenedBy(String openedBy) {
		this.openedBy = openedBy;
	}

	@JsonProperty("division")
	public String getDivision() {
		return division;
	}

	@JsonProperty("division")
	public void setDivision(String division) {
		this.division = division;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	@JsonProperty("number")
	public String getNumber() {
		return number;
	}

	@JsonProperty("number")
	public void setNumber(String number) {
		this.number = number;
	}

	public String getJiraboard() {
		return jiraboard;
	}

	public void setJiraboard(String jiraboard) {
		this.jiraboard = jiraboard;
	}

	@JsonProperty("attachments")
	public List<SNOWAttachment> getAttachments() {
		return attachments;
	}

	@JsonProperty("attachments")
	public void setAttachments(List<SNOWAttachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SnowRequest [title=");
		builder.append(title);
		builder.append(", openedBy=");
		builder.append(openedBy);
		builder.append(", division=");
		builder.append(division);
		builder.append(", description=");
		builder.append(description);
		builder.append(", priority=");
		builder.append(priority);
		builder.append(", number=");
		builder.append(number);
		builder.append(", jiraboard=");
		builder.append(jiraboard);
		builder.append(", attachments=");
		builder.append(attachments);
		builder.append("]");
		return builder.toString();
	}

}