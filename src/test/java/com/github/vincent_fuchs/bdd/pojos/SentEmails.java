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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((recipient == null) ? 0 : recipient.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SentEmails other = (SentEmails) obj;
		if (recipient == null) {
			if (other.recipient != null)
				return false;
		} else if (!recipient.equals(other.recipient))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
	
	
	
}
