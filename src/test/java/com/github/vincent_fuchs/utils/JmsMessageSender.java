package com.github.vincent_fuchs.utils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class JmsMessageSender {
	  

	  private JmsTemplate jmsTemplate;
	    
	    
	  public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	   * send text to default destination
	   * @param text
	   */
	  public void send(final String text) {
	      
	    this.jmsTemplate.send(new MessageCreator() {
	
	      public Message createMessage(Session session) throws JMSException {
	        Message message = session.createTextMessage(text);     
	        //set ReplyTo header of Message, pretty much like the concept of email.
	        message.setJMSReplyTo(new ActiveMQQueue("Recv2Send"));
	        return message;
	      }
	    });
	  }
	   
	
	}