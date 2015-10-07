package com.github.vincent_fuchs.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

import com.github.vincent_fuchs.bdd.pojos.CustomerRequest;
import com.github.vincent_fuchs.targetSystem.CommandsHistoryController;
import com.github.vincent_fuchs.targetSystem.TargetRESTSystem;
import com.github.vincent_fuchs.utils.JmsMessageSender;
import com.github.vincent_fuchs.utils.PropertiesLoader;
import com.github.vincent_fuchs.utils.SpringContextBuilder;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration(classes = TargetRESTSystem.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest({ "server.port=62984" })
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class })
public class UpgradeCustomersStepsDef {

	private static final Logger log = Logger
			.getLogger(UpgradeCustomersStepsDef.class);

	protected ConfigurableApplicationContext appCtx;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	private Properties properties;

	@Autowired
	CommandsHistoryController endpoint;
			
	JmsMessageSender jmsMessageSender ;
   	
	GreenMail mailServer = new GreenMail(ServerSetup.SMTP);
	

	
	@Before(order = 1)
	public void loadSpringCtx() throws IOException {
		
		SpringContextBuilder builder = new SpringContextBuilder().usingContext(new ClassPathResource("upgradeCustomersBatch-core.xml"))
																 .usingContext(new ClassPathResource("upgradeCustomersBatch-datasource-test.xml"))
																 .usingContext(new ClassPathResource("upgradeCustomersBatch-properties-test.xml"));

		appCtx = builder.build();

		jdbcTemplate = appCtx.getBean(JdbcTemplate.class);
		
		jmsMessageSender = appCtx.getBean(JmsMessageSender.class);
			
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
	
	@Given("^we receive status upgrade requests for these customers$")
	public void we_receive_status_upgrade_requests_for_these_customers(List<CustomerRequest> customerRequests) throws Throwable {
	   		
		for(CustomerRequest customerRequest : customerRequests){
			
			System.out.println("pushing message for customer "+customerRequest.getName());
			
			jmsMessageSender.send(customerRequest.getName()+"#"+customerRequest.getEmail());
		}
			
	}
	
	@Given("^command history for users is as followed$")
	public void command_history_for_users_is_as_followed(Map<String,Integer> customerCommandsHistory) throws Throwable {
	    
		endpoint.addAllCommandHistoryRecords(customerCommandsHistory);
		
	}
	
	@When("^customer upgrade batch gets the upgrade requests and we wait \"(.*?)\" seconds$")
	public void customer_upgrade_batch_gets_the_upgrade_requests_and_we_wait_seconds(int timeout) throws Throwable {
			
		Thread.sleep(timeout * 1000);
		
	}
	
	@Then("^customer loyalty repository gets updated with$")
	public void customer_localty_repository_gets_updated_with(Map<String,String> expectedCustomerStatuses) throws Throwable {
	 
		List<Map<String, Object>> actualCustomerLoyaltyRecords = jdbcTemplate
				.queryForList("SELECT * FROM CUSTOMERS_LOYALTY");

		assertThat(actualCustomerLoyaltyRecords).hasSize(expectedCustomerStatuses.size());
		
		for(Map<String, Object> actualCustomerLoyaltyRecord : actualCustomerLoyaltyRecords){
			
			String customerName=(String)actualCustomerLoyaltyRecord.get("name");
			String actualCustomerLoyalty=(String)actualCustomerLoyaltyRecord.get("loyalty");
			
			assertThat(actualCustomerLoyalty).isEqualTo(expectedCustomerStatuses.get(customerName));			
		}

		
		
	}

	


}
