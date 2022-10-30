package com.nextlabs.em.windchill.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Entry implements Serializable {
	
	private String URLPattern;
	
	private String action;
	
	private String httpMethod;
	
	private Map<String,List<String>> queryParameters;

	public String getURLPattern() {
		return URLPattern;
	}

	public void setURLPattern(String uRLPattern) {
		URLPattern = uRLPattern;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Map<String, List<String>> getQueryParameters() {
		return queryParameters;
	}

	public void setQueryParameters(Map<String, List<String>> queryParameters) {
		this.queryParameters = queryParameters;
	}

}
