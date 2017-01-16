package com.github.vincent_fuchs.processors;

import com.oneeyedmen.fakir.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.MailSender;
import org.springframework.web.client.RestTemplate;
import pojos.CustomerStatusRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CustomerUpgradeStatusProcessorTest {

    @Mock
    RestTemplate restTemplate;


    @Mock
    MailSender mailSender;

    @InjectMocks
    CustomerUpgradeStatusProcessor customerUpgradeStatusProcessor;

    final String dummyCustomerName="vincent";

    CustomerStatusRequest customerStatusRequest = new Faker<CustomerStatusRequest>() {
        String customerName = dummyCustomerName;
        String loyaltyStatus = null;
    }.get();

    @Before
    public void setup(){

        customerUpgradeStatusProcessor.setHost("localhost");
        customerUpgradeStatusProcessor.setPort("8080");
        customerUpgradeStatusProcessor.setService("myService");

        when(restTemplate.getForObject("localhost:8080/myService/"+dummyCustomerName, Integer.class)).thenReturn(10);
    }

    @Test
    public void shouldUpgradeStatusToGold_workingWithFakirWhenGoingByFieldValue() throws Exception {

        CustomerStatusRequest actualCustomerStatusRequest=customerUpgradeStatusProcessor.upgradeStatus(customerStatusRequest);

        assertThat(actualCustomerStatusRequest.getLoyaltyStatus()).isEqualTo("Gold");
    }

    @Test
    public void shouldUpgradeStatusToGold_notWorkingBecauseOfFakirIssue5() throws Exception {

        CustomerStatusRequest expectedCustomerStatusRequest = new Faker<CustomerStatusRequest>() {
            String customerName = dummyCustomerName;
            String loyaltyStatus = "Gold";
        }.get();


        CustomerStatusRequest actualCustomerStatusRequest=customerUpgradeStatusProcessor.upgradeStatus(customerStatusRequest);

        assertThat(actualCustomerStatusRequest).isEqualTo(expectedCustomerStatusRequest);
    }

}