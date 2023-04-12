package com.gemini.EJ.tests.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilitySteps {
    EnvironmentSpecificConfiguration envConfig = EnvironmentSpecificConfiguration.from(SystemEnvironmentVariables.createEnvironmentVariables());


    //-----------------------------------------Request building functions-----------------------------------------//

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
        for (Map<String, String> header : headers) {
            UtilityFunctions.addHeader(header.get("Name"), header.get("Value"));
        }
    }

    @When("^User adds Query Parameters$")
    public void addQueryParamsToRequest(DataTable dt) {
        List<Map<String, String>> params = dt.asMaps();
        for (Map<String, String> param : params) {
            UtilityFunctions.addQueryParam(param.get("Name"), param.get("Value"));
        }
    }

    @When("^User adds multiple Query Parameters$")
    public void addMultipleQueryParamsToRequest(DataTable dt) {
        List<Map<String, String>> params = dt.asMaps();
        HashMap<String, String> param = new HashMap<>();
        for (String key : params.get(0).keySet()) {
            param.put(key, params.get(0).get(key));
        }
        UtilityFunctions.addQueryParams(param);
    }

    @When("^User sets Content-Type as \"(.*?)\"$")
    public void setContentType(String contentType) {
        UtilityFunctions.setContentType(contentType);
    }

    @Then("^User sets relaxed HTTPS validation$")
    public void setRelaxedHttpsValidation() {
        UtilityFunctions.setRelaxedHttpsValidation();
    }

    @Then("^User sets url encoding to \"(.*?)\"$")
    public void setUrlEncoding(boolean encodingStatus) {
        UtilityFunctions.setUrlEncoding(encodingStatus);
    }

    @And("^user sets request body from \"(.*?)\" file$")
    public void userSetsRequestBodyFromFile(String fileName) {
        UtilityFunctions.userSetsRequestBodyFromFile(fileName);
    }

    @And("^user adds multipart file \"(.*?)\" with \"(.*?)\" control name$")
    public void addMultipartFileToRequest(String fileName, String controlName) {
        UtilityFunctions.addMultipartFileToRequest(controlName, fileName);
    }

    @And("^user adds multipart file \"(.*?)\" with \"(.*?)\" control name and \"(.*?)\" MIME type$")
    public void addMultipartFileToRequest(String fileName, String controlName, String mimeType) {
        UtilityFunctions.addMultipartFileToRequest(controlName, fileName, mimeType);
    }

    @Then("^User makes a \"(.*?)\" request$")
    public void userMakesARequest(String reqType) {
        UtilityFunctions.makeRequest(reqType);
    }


    //-----------------------------------------Validation Functions-----------------------------------------//
    @When("^User verifies value \"(.*?)\" is found in response for \"(.*?)\" key for latest response$")
    public void verifyValueFromJson(String expectedValue, String keyName) {
        UtilityFunctions.verifyKeyAndValuePresence(keyName, expectedValue);
    }

    @When("^User verifies value \"(.*?)\" is found in response for \"(.*?)\" key for Response number \"(.*?)\"$")
    public void verifyValueFromJsonWithSpecifiedResponse(String expectedValue, String keyName, String requestNo) {
        UtilityFunctions.verifyKeyAndValuePresence(keyName, expectedValue, requestNo);
    }

    @When("^User verifies value \"(.*?)\" is found in response for \"(.*?)\" path for latest response$")
    public void verifyValueFromJsonWithPath(String expectedValue, String pathProvided) {
        UtilityFunctions.verifyKeyAndValuePresence(pathProvided, expectedValue);
    }

    @When("^User verifies value \"(.*?)\" is found in response for \"(.*?)\" path for Response number \"(.*?)\"$")
    public void verifyValueFromJsonWithPathWithSpecifiedResponse(String expectedValue, String pathProvided, String responseNo) {
        UtilityFunctions.verifyKeyAndValuePresence(pathProvided, expectedValue, responseNo);
    }

    @And("^User verifies the response matches \"(.*?)\" schema for latest response$")
    public void verifySchemaMatches(String schemaName) {
        UtilityFunctions.verifyResponseSchema(schemaName);
    }

    @And("^User verifies the response matches \"(.*?)\" schema for Response number \"(.*?)\"$")
    public void verifySchemaMatchesWithSpecifiedResponse(String schemaName, String responseNo) {
        UtilityFunctions.verifyResponseSchema(schemaName, responseNo);
    }

    @And("^User verifies the size of array with key \"(.*?)\" is \"(.*?)\" in latest response$")
    public void verifyArraySizeForKey(String keyName, String size) {
        try {
            UtilityFunctions.verifyJsonArraySize(keyName, Integer.parseInt(size));
        } catch (Exception e) {
            Assert.fail("Invalid size format. Please recheck.");
        }
    }

    @And("^User verifies the size of array with key \"(.*?)\" is \"(.*?)\" for Response number \"(.*?)\"$")
    public void verifyArraySizeForKeyWithSpecifiedResponse(String keyName, String size, String responseNo) {
        try {
            UtilityFunctions.verifyJsonArraySize(keyName, Integer.parseInt(size), responseNo);
        } catch (Exception e) {
            Assert.fail("Invalid size format. Please recheck.");
        }
    }

    @And("^User verifies the response time taken is less than \"(.*?)\" seconds in latest response$")
    public void verifyResponseTime(String expectedTime) {
        try {
            UtilityFunctions.verifyResponseTime(Float.parseFloat(expectedTime));
        } catch (Exception e) {
            Assert.fail("Invalid time format. Please recheck.");
        }
    }

    @And("^User verifies the response time taken is less than \"(.*?)\" seconds for Response number \"(.*?)\"$")
    public void verifyResponseTimeWithSpecifiedResponse(String expectedTime, String responseNo) {
        try {
            UtilityFunctions.verifyResponseTime(Float.parseFloat(expectedTime), responseNo);
        } catch (Exception e) {
            Assert.fail("Invalid time format. Please recheck.");
        }
    }

    @And("^User verifies the presence of message \"(.*?)\" in latest response body$")
    public void verifyResponseMessage(String expectedMessage) {
        UtilityFunctions.messageValidation(expectedMessage, UtilityFunctions.responseList.get(UtilityFunctions.activeIndex));
    }

    @And("^User verifies the presence of message \"(.*?)\" in response body for Response number \"(.*?)\"$")
    public void verifyResponseMessageWithSpecifiedResponse(String expectedMessage, String responseNo) {
        UtilityFunctions.messageValidation(expectedMessage, responseNo);
    }

    @And("^User verifies response code as \"(.*?)\" for latest response$")
    public void userVerifiesResponseCodeAs(String expectedCode) {
        UtilityFunctions.verifyStatusCode(Integer.parseInt(expectedCode));
    }

    @And("^User verifies response code as \"(.*?)\" for Response number \"(.*?)\"$")
    public void userVerifiesResponseCodeWithSpecifiedResponse(String expectedCode, String responseNo) {
        UtilityFunctions.verifyStatusCode(Integer.parseInt(expectedCode), responseNo);
    }

    @And("^User verifies value \"(.*?)\" is found in Json array response for \"(.*?)\" key in latest response$")
    public void verifyValueInJsonArray(String expectedValue, String keyName) {
        UtilityFunctions.verifyValueInJsonArray(keyName, expectedValue, UtilityFunctions.responseList.get(UtilityFunctions.activeIndex));
    }

    @And("^User verifies value \"(.*?)\" is found in Json array response for \"(.*?)\" key for Response number \"(.*?)\"$")
    public void verifyValueInJsonArrayWithSpecifiedResponse(String expectedValue, String keyName, String responseNo) {
        UtilityFunctions.verifyValueInJsonArray(keyName, expectedValue, responseNo);
    }

    @And("^User verifies value \"(.*?)\" is found in Json array response for key with Json Path \"(.*?)\" in latest response$")
    public void verifyValueInJsonArrayWithPath(String expectedValue, String jsonPath) {
        UtilityFunctions.verifyValueInJsonArray(jsonPath, expectedValue, UtilityFunctions.responseList.get(UtilityFunctions.activeIndex));
    }

    @And("^User verifies value \"(.*?)\" is found in Json array response for key with Json Path \"(.*?)\" for Response number \"(.*?)\"$")
    public void verifyValueInJsonArrayWithPathWithSpecifiedResponse(String expectedValue, String jsonPath, String responseNo) {
        UtilityFunctions.verifyValueInJsonArray(jsonPath, expectedValue, responseNo);
    }
}
