package com.gemini.EJ.tests.runner;


import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features"
        ,glue = "com.gemini.EJ.tests.stepdefinitions"
        ,tags="@RequestTest"
)
public class RestAssuredRunner {
}
