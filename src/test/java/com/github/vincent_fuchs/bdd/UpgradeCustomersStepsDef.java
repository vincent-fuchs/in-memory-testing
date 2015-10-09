package com.github.vincent_fuchs.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.github.vincent_fuchs.bdd.pojos.CustomerRequest;
import com.github.vincent_fuchs.bdd.pojos.SentEmails;
import com.github.vincent_fuchs.targetSystem.CommandsHistoryController;
import com.github.vincent_fuchs.targetSystem.TargetRESTSystem;
import com.github.vincent_fuchs.utils.JmsMessageSender;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration(classes = TargetRESTSystem.class, 
					  loader = SpringApplicationContextLoader.class,
					  locations={"upgradeCustomersBatch-core.xml",
								 "upgradeCustomersBatch-datasource-test.xml",
								 "upgradeCustomersBatch-properties-test.xml"}
					  
					  )
@WebIntegrationTest({ "server.port=62984" })
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class })
public class UpgradeCustomersStepsDef {

	private static final Logger log = Logger
			.getLogger(UpgradeCustomersStepsDef.class);

	@Autowired
	protected ConfigurableApplicationContext appCtx;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	CommandsHistoryController endpoint;
	
	@Autowired
	JmsMessageSender jmsMessageSender ;
   	
	GreenMail mailServer = new GreenMail(ServerSetup.SMTP);

	
	@Before(order = 1)
	public void startGreenMailServer(){
		mailServer.start();
		System.out.println("mail server started");
	}
	
	@Before(order = 2)
	public void initCustomerLoyaltyTable() throws Exception {

		int nbRowsDeleted = jdbcTemplate.update("DELETE from customers_loyalty");
		log.info("deleted " + nbRowsDeleted
				+ " deal(s) in customers_loyalty table before scenario starts");
		
	}

	@Before(order = 3)
	public void resetTargetHost() throws Exception {
		endpoint.resetConfiguredCommandsPerCustomer();

		// in case a test has set this to true, resetting it to false
		endpoint.setShouldThrowExceptionOnReceivingRequests(false);
	}

	
	@After(order=1)
	public void stopGreenMailServer(){
		mailServer.stop();
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
	
	@Then("^emails are sent to people with following subject$")
	public void emails_are_sent_to_people_with_following_subject(List<SentEmails> expectedSentEmails) throws Throwable {
	   
		
		MimeMessage[] actualSentEmailsFromServer=mailServer.getReceivedMessages();
		
		List<SentEmails> actualSentEmailsToCompare=new ArrayList<SentEmails>();
		
		for(MimeMessage actualEmail : actualSentEmailsFromServer){
						
			actualSentEmailsToCompare.add(new SentEmails(actualEmail.getAllRecipients(),actualEmail.getSubject()));
					
		}
		
		assertThat(actualSentEmailsToCompare).containsAll(expectedSentEmails);
		
	}

	


}
