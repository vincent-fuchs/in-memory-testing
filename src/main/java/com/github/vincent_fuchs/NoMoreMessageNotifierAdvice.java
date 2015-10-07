package com.github.vincent_fuchs;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;


/**
 * When applied on a poller, allows to take action depending on whether a message has been retrieved or not and 
 *
 */

public class NoMoreMessageNotifierAdvice implements MethodInterceptor  {
	
	private static final Logger log = Logger.getLogger(NoMoreMessageNotifierAdvice.class);
	
	private NoMoreMessageAction actionHandler;

	public Object invoke(MethodInvocation invocation) throws Throwable {
				
		Object result=invocation.proceed();
		
		if(result instanceof Boolean){
			
			boolean hasPolledAMessage=(Boolean)result;
			
			if(!hasPolledAMessage){
				log.info("poller got no message during last poll -> calling configured action");
				
				actionHandler.handle();	
			}
						
		}
			
		return result;
	}


	public void setActionHandler(NoMoreMessageAction actionHandler) {
		this.actionHandler = actionHandler;
	}

	

}
