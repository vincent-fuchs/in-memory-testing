package com.github.vincent_fuchs;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features="classpath:features/upgradeCustomers.feature",
				 strict=true
				 )
public class RunCucumberTest {



}