This sample application shows how to use various libraries to perform efficient integration testing using in memory components.

## What the application does, and what we need to test

The application that we implemented and tested is a batch (or an interface), that will process requests from customers to upgrade their loyalty level. Let's imagine that on our web site, customers can make that request by simply clicking on a link. Since the operation itself may take some time, we need to be asynchronous, so a message will be put in a queue, and a message will be displayed on screen saying something "request taken into account, you'll receive a confirmation email shortly".

Now, this is what we need to implement in our batch :

1. pick messages from queue - message contains user name and email address
2. process the message : loyalty status will depend on customer history - which is hosted on a REST webservice, managed by another team
3. once loyalty status has been determined (gold, silver, etc), save it in DB 
4. send a confirmation email to customer. 
5. send an email to support team in case of an issue
6. log errors in file for our monitoring platform to pick up the issue 


The only business logic to implement is "what loyalty status should be given to a a customer, based on his/her history". The rest is "plumbing" code and doesn't have much business value. A messaging framework like Spring Integration (or Apache Camel) will help us to take care of that plumbing, and let us focus on our business logic. 

However, we still need to be able to validate quickly that it has the expected behavior, because even if there's not much code, there will be a lot of Spring configuration, and a minor change in it could break some features of our batch.

## To achieve all this "blackbox" testing, we'll use

* in memory queue - Apache ActiveMQ
* in memory DB - H2
* in memory mail server - Greenmail
* in memory logger - with a trick using regular log4J / logBack

We'll also start up web service locally, that will act as a double of real one, using Spring Boot

We'll also use Cucumber to drive the scenario and make sure with all stakeholders that we are on same page. 

Scenario is available here, with an example showing the business rule and the other expectations : [upgradeCustomers.feature](./src/test/resources/features/upgradeCustomers.feature)

## Disclaimer

* You may find the implementation "not great", not leveraging enough on Spring integration model.. But the good thing is that now that you have an integration test that covers everything, you can improve the code and/or config, and run the test to know if you broke something. 

* Depending on the machine on which you run the test, you may have to increase the waiting time in the scenario 

* If you find a way to close cleanly the MQ connection, feel fee to send me a PR. Right now, the test is successful, but it crashes badly when the test is closing down the resources. Would love to avoid that nasty stacktrace !




