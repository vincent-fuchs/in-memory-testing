package com.github.vincent_fuchs;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import pojos.CustomerStatusRequest;

public class ConfirmationEmailSender {

	MailSender mailSender;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendEmail(CustomerStatusRequest request) {
	
		

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(request.getCustomerEmail());
		mailMessage.setSubject("you have just been upgraded to "+request.getLoyaltyStatus()+" status");
		
		mailSender.send(mailMessage);
		
		System.out.println("email sent to "+request.getCustomerEmail());

	}

}
