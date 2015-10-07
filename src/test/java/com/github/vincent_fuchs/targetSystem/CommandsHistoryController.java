package com.github.vincent_fuchs.targetSystem;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandsHistoryController {

	private boolean shouldThrowExceptionOnReceivingRequests=false;
	
	private Map<String,Integer> configuredCommandsPerCustomer = new HashMap<String, Integer>();
	
	public void setShouldThrowExceptionOnReceivingRequests(
			boolean makeServerUnavailable) {
		this.shouldThrowExceptionOnReceivingRequests = makeServerUnavailable;
	}

	public void addCommandHistoryRecord(String customer, int nbCommands) {
		
		configuredCommandsPerCustomer.put(customer,nbCommands);
		
	}
	
	public void resetConfiguredCommandsPerCustomer() {
		configuredCommandsPerCustomer.clear();
	}

	@RequestMapping(method = RequestMethod.GET)
	int acceptColExpirationRequestForDeal(@RequestBody String customerName) {
		
		if(shouldThrowExceptionOnReceivingRequests){
			throw new RuntimeException("rejecting all requests, as per test configuration");
		}
		
		return configuredCommandsPerCustomer.get(customerName);
	}

}
