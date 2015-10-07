package com.github.vincent_fuchs.processors;

import org.springframework.web.client.RestTemplate;

import pojos.CustomerStatusRequest;

public class CustomerUpgradeStatusProcessor {

	private RestTemplate restTemplate=new RestTemplate();
			
	private String host;
	private String port;
	private String service;
	
	public CustomerStatusRequest upgradeStatus(CustomerStatusRequest request){
		
		Integer nbCommands = restTemplate.getForObject(host+":"+port+"/"+service+"/"+request.getCustomerName(), Integer.class);
	      
		
		
		return request;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	
	
	
}
