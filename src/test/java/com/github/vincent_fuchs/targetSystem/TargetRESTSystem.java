package com.github.vincent_fuchs.targetSystem;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;


@Configuration
@ComponentScan
@EnableAutoConfiguration
public class TargetRESTSystem {
 
	private static AbstractApplicationContext  appCtx;

	
    public void stop(){
    	
    	appCtx.close();
    	
    }
}

