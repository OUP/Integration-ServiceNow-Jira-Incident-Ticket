package com.oup.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"com.oup.integration"})
//@SpringBootApplication
public class SnowToJiraIncidentTicketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnowToJiraIncidentTicketApplication.class, args);
	}
}
