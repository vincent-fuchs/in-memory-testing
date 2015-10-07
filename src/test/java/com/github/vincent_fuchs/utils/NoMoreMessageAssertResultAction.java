package com.github.vincent_fuchs.utils;

import org.apache.log4j.Logger;

public class NoMoreMessageAssertResultAction implements com.github.vincent_fuchs.NoMoreMessageAction {

	private boolean stopWaitingForMessageProcessingCompletion=true;
	
	private static final Logger log = Logger.getLogger(NoMoreMessageAssertResultAction.class);
	
	public boolean shouldKeepWaitingForMessageProcessingCompletion() {
		return stopWaitingForMessageProcessingCompletion;
	}

	public void handle() {
		
		log.debug("Setting flag so that test stops waiting for messages to be processed");
		stopWaitingForMessageProcessingCompletion=false;
		

	}

}
