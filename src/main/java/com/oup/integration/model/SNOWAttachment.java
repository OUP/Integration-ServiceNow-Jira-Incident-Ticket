package com.oup.integration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"name",
"content",
"contentType"
})
public class SNOWAttachment {

@JsonProperty("attachmentName")
private String attachmentName;
@JsonProperty("attachmentId")
private String attachmentId;
@JsonProperty("attachmentContentType")
private String attachmentContentType;
public String getAttachmentName() {
	return attachmentName;
}
public void setAttachmentName(String attachmentName) {
	this.attachmentName = attachmentName;
}
public String getAttachmentId() {
	return attachmentId;
}
public void setAttachmentId(String attachmentId) {
	this.attachmentId = attachmentId;
}
public String getAttachmentContentType() {
	return attachmentContentType;
}
public void setAttachmentContentType(String attachmentContentType) {
	this.attachmentContentType = attachmentContentType;
}
@Override
public String toString() {
	return "SNOWAttachment [attachmentName=" + attachmentName + ", attachmentId=" + attachmentId
			+ ", attachmentContentType=" + attachmentContentType + "]";
}



}


