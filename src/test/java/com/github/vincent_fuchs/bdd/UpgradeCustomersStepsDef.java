package com.github.vincent_fuchs.bdd;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.github.vincent_fuchs.targetSystem.CommandsHistoryController;
import com.github.vincent_fuchs.targetSystem.TargetRESTSystem;
import com.github.vincent_fuchs.utils.PropertiesLoader;
import com.github.vincent_fuchs.utils.SpringContextBuilder;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import cucumber.api.java.After;
import cucumber.api.java.Before;

@ContextConfiguration(classes = TargetRESTSystem.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest({ "server.port=62984" })
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class })
public class UpgradeCustomersStepsDef {

	private static final Logger log = Logger
			.getLogger(UpgradeCustomersStepsDef.class);

	protected ConfigurableApplicationContext appCtx;

	protected JdbcTemplate jdbcTemplate;

	private Properties properties;

	@Autowired
	CommandsHistoryController endpoint;
	
	GreenMail mailServer = new GreenMail(ServerSetup.SMTP);
	

	
	@Before(order = 1)
	public void loadSpringCtx() throws IOException {
		
		SpringContextBuilder builder = new SpringContextBuilder().usingContext(new ClassPathResource("upgradeCustomersBatch-core.xml"))
																 .usingContext(new ClassPathResource("upgradeCustomersBatch-datasource-test.xml"))
																 .usingContext(new ClassPathResource("upgradeCustomersBatch-properties-test.xml"));

		appCtx = builder.build();

		jdbcTemplate = (JdbcTemplate) appCtx.getBean("jdbcTemplate");
			
	}

	@Before(order = 2)
	public void initProperties() throws IOException {

		properties = PropertiesLoader.load("/upgradeCustomersBatch-test.properties");
		properties.getProperty("lso.updateStatusEndpoint.url");
	}

	@Before(order = 4)
	public void initCustomerLoyaltyTable() throws Exception {

		int nbRowsDeleted = jdbcTemplate.update("DELETE from customers_loyalty");
		log.info("deleted " + nbRowsDeleted
				+ " deal(s) in customers_loyalty table before scenario starts");
	}

	@Before(order = 5)
	public void resetTargetHost() throws Exception {
		endpoint.resetConfiguredCommandsPerCustomer();

		// in case a test has set this to true, resetting it to false
		endpoint.setShouldThrowExceptionOnReceivingRequests(false);
	}
	
	@Before(order = 6)
	public void startGreenMailServer(){
		mailServer.start();
	}
	
	@After(order=1)
	public void stopGreenMailServer(){
		mailServer.stop();
	}

	@After(order=2)
	public void teardown() throws Exception {

		if (appCtx != null) {
			appCtx.close();
		}
	}
	
	
	


}
