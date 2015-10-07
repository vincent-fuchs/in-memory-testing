package com.github.vincent_fuchs;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;


public class UpgradeCustomersBatchErrorHandler {

	private static final Logger log = Logger.getLogger(UpgradeCustomersBatchErrorHandler.class);
		
	MailSender batchMailService;
	
	private NoMoreMessageAction actionHandler;
	
	public void setActionHandler(NoMoreMessageAction actionHandler) {
		this.actionHandler = actionHandler;
	}

	public void handleError(Message<MessagingException> message){
		log.warn("Exception occured : " + message);
	//	batchMailService.sendPreConfiguredMail(EMAIL_SUBJECT, ERROR_MSG);
		actionHandler.handle();
	}

}
