package com.github.vincent_fuchs.converters;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import pojos.CustomerStatusRequest;

public class InputRequestConverter implements MessageConverter{

	public CustomerStatusRequest fromMessage(Message message) throws JMSException {
		
		String msgPayload=((TextMessage)message).getText();
		
		String[] fields=msgPayload.split("#");
		
		
		CustomerStatusRequest customerStatusRequest=new CustomerStatusRequest();
		customerStatusRequest.setCustomerName(fields[0]);
		customerStatusRequest.setCustomerEmail(fields[1]);
		
		return customerStatusRequest;
	}

	public Message toMessage(Object arg0, Session arg1) throws JMSException,
			MessageConversionException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
