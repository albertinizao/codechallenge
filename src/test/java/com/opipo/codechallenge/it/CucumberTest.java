package com.opipo.codechallenge.it;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/cucumber", plugin = {"pretty", "json:target/cucumber-report.json"})
public class CucumberTest {

}
