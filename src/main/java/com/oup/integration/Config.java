package com.oup.integration;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oup.integration.model.jira.transitions.toTransitionsList;

@Configuration
public class Config {

	
    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {

            	toTransitionsList myClass = new toTransitionsList();
            	context.getTypeConverterRegistry().addTypeConverters(myClass);
            }
			@Override
			public void afterApplicationStart(CamelContext camelContext) {
				// nothing yet
				
			}
        };
    }
}
