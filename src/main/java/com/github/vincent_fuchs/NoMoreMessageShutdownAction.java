package com.github.vincent_fuchs;

import org.apache.log4j.Logger;

public class NoMoreMessageShutdownAction implements NoMoreMessageAction {

	private static final Logger log = Logger.getLogger(NoMoreMessageShutdownAction.class);
	

	public void handle() {
		
		log.info("shutting down the application");
		
		UpgradeCustomersBatch.stop();

	}

}
