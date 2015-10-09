package com.github.vincent_fuchs.processors;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.client.RestTemplate;

import pojos.CustomerStatusRequest;

public class CustomerUpgradeStatusProcessor {

	private RestTemplate restTemplate=new RestTemplate();

	private MailSender mailSender;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	
	private String host;
	private String port;
	private String service;
	
	public CustomerStatusRequest upgradeStatus(CustomerStatusRequest request) {
		
		Integer nbCommands = restTemplate.getForObject(host+":"+port+"/"+service+"/"+request.getCustomerName(), Integer.class);
	      
		if(nbCommands<=0){

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo("support@my-store.com");
			mailMessage.setSubject("invalid upgrade request for customer "
					+ request.getCustomerName());
			mailMessage.setText("blabla");

			mailSender.send(mailMessage);
			
			return null;
		
		}
		else if(nbCommands>5){
			request.setLoyaltyStatus("Gold");
		}
		else if(nbCommands<=5){
			request.setLoyaltyStatus("Silver");
		}
		
		
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
