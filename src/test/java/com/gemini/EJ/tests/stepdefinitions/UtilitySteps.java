package com.gemini.EJ.tests.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class UtilitySteps {
    Response response;
    EnvironmentSpecificConfiguration envConfig = EnvironmentSpecificConfiguration.from(SystemEnvironmentVariables.createEnvironmentVariables());

    @Given("user hits local server")
    public void userMakesARequest() {
        response = SerenityRest.given()
                .baseUri(envConfig.getProperty("baseURI"))
                .basePath(envConfig.getProperty("basePath"))
                .log().all()
                .when().get();

        System.out.println("------------------\nResponse Body:\n" + response.getBody().asPrettyString() + "\n------------------");
    }

    //Request building functions

    @When("^User creates a new request$")
    public void createNewRequest() {
        UtilityFunctions.createNewRequest();
    }

    @When("^User sets base URI as \"(.*?)\"$")
    public void setBaseURI(String URI) {
        UtilityFunctions.setBaseURI(URI);
    }

    @When("^User sets path as \"(.*?)\"$")
    public void setBasePath(String path) {
        UtilityFunctions.setBasePath(path);
    }

    @When("^User adds headers$")
    public void addHeadersToRequest(DataTable dt) {
        List<Map<String, String>> headers = dt.asMaps();
        for (Map<String, String> header : headers)
            UtilityFunctions.addHeader(header.get("Name"),header.get("Value"));
    }

    @When("^User adds Query Parameters$")
    public void addQueryParamsToRequest(DataTable dt) {
        List<Map<String, String>> params = dt.asMaps();
        for (Map<String, String> param : params)
            UtilityFunctions.addQueryParam(param.get("Name"),param.get("Value"));
    }

    @When("^User sets Content-Type as \"(.*?)\"$")
    public void setContentType(String contentType) {
        UtilityFunctions.setContentType(contentType);
    }

    @Then("^user sets relaxed HTTPS validation$")
    public void setRelaxedHttpsValidation() {
        UtilityFunctions.setRelaxedHttpsValidation();
    }

    @Then("^user sets url encoding to \"(.*?)\"$")
    public void setUrlEncoding(boolean encodingStatus) {
        UtilityFunctions.setUrlEncoding(encodingStatus);
    }

    @Then("^user makes a \"(.*?)\" request$")
    public void userMakesARequest(String reqType) {
        UtilityFunctions.makeRequest(reqType);
    }

    //Validation Functions
    @When("^User verifies value \"(.*?)\" is found in response for \"(.*?)\" key$")
    public void verifyValueFromJson(String expectedValue, String keyName) {
        UtilityFunctions.verifyKeyAndValuePresence(keyName, expectedValue, response);
    }

    @And("^User verifies the response matches \"(.*?)\" schema$")
    public void verifySchemaMatches(String schemaName) {
        UtilityFunctions.verifyResponseSchema(schemaName, response);
    }

    @And("^User verifies the size of array with key \"(.*?)\" is \"(.*?)\"$")
    public void verifyArraySizeForKey(String keyName, String size) {
        try {
            UtilityFunctions.verifyJsonArraySize(keyName, Integer.parseInt(size), response);
        } catch (Exception e) {
            Assert.fail("Invalid size format. Please recheck.");
        }
    }

    @And("^User verifies the response time taken is less than \"(.*?)\" seconds$")
    public void verifyResponseTime(String expectedTime) {
        try {
            UtilityFunctions.verifyResponseTime(Long.parseLong(expectedTime), response);
        } catch (Exception e) {
            Assert.fail("Invalid time format. Please recheck.");
        }
    }

    @And("^User verifies the presence of message \"(.*?)\" in response body$")
    public void verifyResponseMessage(String expectedMessage) {
        UtilityFunctions.messageValidation(expectedMessage, response);
    }

    @And("^user verifies response code as \"(.*?)\"$")
    public void userVerifiesResponseCodeAs(String expectedCode) {
        UtilityFunctions.verifyStatusCode(Integer.parseInt(expectedCode), response);
    }

    @And("^User verifies value \"(.*?)\" is found in Json array response for \"(.*?)\" key$")
    public void verifyValueInJsonArray(String expectedValue, String keyName) {
        UtilityFunctions.verifyValueInJsonArray(keyName, expectedValue, response);
    }

    @And("^User verifies value \"(.*?)\" is found in Json array response for key with Json Path \"(.*?)\"$")
    public void verifyValueInJsonArrayWithPath(String expectedValue, String jsonPath) {
        UtilityFunctions.verifyValueInJsonArray(jsonPath, expectedValue, response);
    }

}
