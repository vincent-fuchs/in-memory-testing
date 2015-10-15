package com.github.vincent_fuchs.processors;

import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.classic.Logger;


import pojos.CustomerStatusRequest;

public class CustomerUpgradeStatusProcessor {

	private RestTemplate restTemplate=new RestTemplate();

	private MailSender mailSender;
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(CustomerUpgradeStatusProcessor.class);


	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	
	private String host;
	private String port;
	private String service;
	
	public CustomerStatusRequest upgradeStatus(CustomerStatusRequest request) {
		
		Integer nbCommands = restTemplate.getForObject(host+":"+port+"/"+service+"/"+request.getCustomerName(), Integer.class);
	      
		if(nbCommands<=0){

			log.error("invalid upgrade request");
			
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

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	
	
	
}
