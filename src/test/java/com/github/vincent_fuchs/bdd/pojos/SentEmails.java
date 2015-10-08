package com.github.vincent_fuchs.bdd.pojos;

import javax.mail.Address;

public class SentEmails {

	
	public SentEmails(Address[] allRecipients, String subject) {
		
		this.subject=subject;
		
		for(Address recipient : allRecipients){
			this.recipient=recipient.toString();
		}
		
		
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	private String recipient;
	
	private String subject;
	
	public String toString(){
		
		return "to: "+recipient+" - subject: "+subject;
		
	}
	
}
