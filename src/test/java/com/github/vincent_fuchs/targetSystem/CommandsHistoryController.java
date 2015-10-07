package com.github.vincent_fuchs.targetSystem;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandsHistoryController {

	private boolean shouldThrowExceptionOnReceivingRequests=false;
	
	private Map<String,Integer> configuredCommandsPerCustomer = new HashMap<String, Integer>();
	
	public void setShouldThrowExceptionOnReceivingRequests(
			boolean makeServerUnavailable) {
		this.shouldThrowExceptionOnReceivingRequests = makeServerUnavailable;
	}

		
	public void addAllCommandHistoryRecords(Map<String,Integer> commandsPerCustomer) {
		
		configuredCommandsPerCustomer.putAll(commandsPerCustomer);
		
	}
	
	public void resetConfiguredCommandsPerCustomer() {
		configuredCommandsPerCustomer.clear();
	}

	@RequestMapping(value = "/commands/{customerName}", method = RequestMethod.GET)
	int acceptColExpirationRequestForDeal(@PathVariable("customerName") String customerName) {
		
		if(shouldThrowExceptionOnReceivingRequests){
			throw new RuntimeException("rejecting all requests, as per test configuration");
		}
		
		System.out.println("receiving a REST request for customer "+customerName);
		
		return configuredCommandsPerCustomer.get(customerName);
	}

}
