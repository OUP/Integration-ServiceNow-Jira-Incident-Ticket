package com.oup.integration.model;

public class JiraIssue {
	
	public JiraIssue() {
		//JiraRestClient restClient=null;
		System.getProperties().put("https.proxyHost", "ouparray.oup.com");
		System.getProperties().put("https.proxyPort", "8080");
	}
}
