package com.github.vincent_fuchs;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import pojos.CustomerStatusRequest;

public class ConfirmationEmailSender {

	JavaMailSender mailSender;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public CustomerStatusRequest sendEmail(CustomerStatusRequest request) {
	
		

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(request.getCustomerEmail());
		mailMessage.setSubject("you have just been upgraded to "+request.getLoyaltyStatus()+" status");
		mailMessage.setText("blabla");
	    		
		mailSender.send(mailMessage);
		
		return request;

	}

}
