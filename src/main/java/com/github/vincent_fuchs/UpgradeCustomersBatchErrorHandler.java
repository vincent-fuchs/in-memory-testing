package com.github.vincent_fuchs;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import pojos.CustomerStatusRequest;

import com.github.vincent_fuchs.processors.InvalidCustomerUpgradeRequest;

public class UpgradeCustomersBatchErrorHandler {

	private static final Logger log = Logger
			.getLogger(UpgradeCustomersBatchErrorHandler.class);

	MailSender mailSender;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void handleError(Message<MessagingException> message) {
		log.warn("Exception occured : " + message);

		Throwable exception = message.getPayload().getCause();

		if (exception instanceof InvalidCustomerUpgradeRequest) {

			log.error(exception);

		}

		Message<CustomerStatusRequest> failedMessage = (Message<CustomerStatusRequest>) message
				.getPayload().getFailedMessage();

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo("support@my-store.com");
		mailMessage.setSubject("invalid upgrade request for customer "
				+ failedMessage.getPayload().getCustomerName());

		mailSender.send(mailMessage);

	}

}
