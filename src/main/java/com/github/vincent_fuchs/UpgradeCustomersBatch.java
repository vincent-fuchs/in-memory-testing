package com.github.vincent_fuchs;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("upgradeCustomers.xml")
public class UpgradeCustomersBatch {

	public static void main(String[] args) {
		SpringApplication.run(UpgradeCustomersBatch.class, args);
	}

	public static void stop() {
		// close application
		System.exit(0);
	}
}