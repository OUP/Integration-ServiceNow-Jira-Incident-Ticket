package com.oup.integration.model.jira.transitions;

import java.util.List;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverters;
import org.apache.camel.converter.stream.InputStreamCache;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class toTransitionsList implements TypeConverters {
	
   @Converter
   public static TransitionList toTransitionList(byte[] data,
	                                                Exchange exchange) {
	 //       TypeConverter converter = exchange.getContext()
	 //                                 .getTypeConverter();
	        DocumentContext Jpayload = JsonPath.parse(data); //converter.convertTo(String.class, data));
	        TransitionList S = new TransitionList();
	        List<Object> L = Jpayload.read("$.transitions");
	        for(Object item : L) {
	@SuppressWarnings("unchecked") // output from jsonpath is known safe (it would have raised an error on bad JSON already)
	        	Map<String, String> map = (Map<String, String>) item;
	        	S.getTransitions().add(new Transition(
	        			map.get("id"),
	        			map.get("name"),
	        			map.get("to"),
	        			map.get("hasScreen"),
	        			map.get("isGlobal"),
	        			map.get("isInitial"),
	        			map.get("isAvailable"),
	        			map.get("isConditional")
	        			));
	        }
	        return S;
	 }

   @Converter
   public static TransitionList toTransitionList(InputStreamCache data,
	                                                Exchange exchange) {
	 //       TypeConverter converter = exchange.getContext()
	 //                                 .getTypeConverter();
	        DocumentContext Jpayload = JsonPath.parse(data); //converter.convertTo(String.class, data));
	        TransitionList S = new TransitionList();
	        List<Object> L = Jpayload.read("$.transitions");
	        for(Object item : L) {
	@SuppressWarnings("unchecked") // output from jsonpath is known safe (it would have raised an error on bad JSON already)
	        	Map<String, String> map = (Map<String, String>) item;
	        	S.getTransitions().add(new Transition(
	        			map.get("id"),
	        			map.get("name"),
	        			map.get("to"),
	        			map.get("hasScreen"),
	        			map.get("isGlobal"),
	        			map.get("isInitial"),
	        			map.get("isAvailable"),
	        			map.get("isConditional")
	        			));
	        }
	        return S;
	 }   
   
   
}
