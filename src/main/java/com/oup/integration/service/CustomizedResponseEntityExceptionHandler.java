package com.oup.integration.service;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.atlassian.jira.rest.client.api.RestClientException;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	@ExceptionHandler(RestClientException.class)
	public final ResponseEntity<String> handleAllExceptions(RestClientException ex) {
	  return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}	

}
