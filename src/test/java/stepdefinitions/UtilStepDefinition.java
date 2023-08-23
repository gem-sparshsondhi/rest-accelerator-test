package stepdefinitions;

import Common.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.environment.SystemEnvironmentVariables;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

public class UtilStepDefinition extends CommonFunctions {
    @Given("^user creates a new request named \"(.*)\" request and sets \"(.*)\" as endpoint$")
    public void createNewRequestAndSetEndpoint(String key, String endpoint) {
        userCreatesRequest(key, endpoint);
    }

    @And("^user makes a \"(.*)\" request$")
    public void userMakesRequest(String method) {
        userHitsRequest(method);
    }

    @And("^user makes \"(.*)\" request for \"(.*)\" request$")
    public void userMakesRequest(String method, String requestKey) {
        userHitsRequest(method, requestKey);
    }

    @Then("^user adds headers to the request$")
    public void addHeadersToRequest(DataTable dataTable) {
        addHeaders(dataTable);
    }

    @Then("^user adds headers to \"(.*?)\" request$")
    public void addHeadersToRequest(String requestKey, DataTable dt) {
        addHeaders(requestKey, dt);
    }

    @Then("^user verifies \"(.*)\" status code$")
    public void userVerifiesStatusCode(int statusCode) {
        verifyStatusCode(statusCode);
    }

    @Then("^user verifies response code as \"(.*)\" for \"(.*)\" response$")
    public void userVerifiesStatusCode(int statusCode, String responseKey) {
        verifyStatusCode(statusCode, responseKey);
    }

    @And("^user add Query Params to \"(.*)\" request$")
    public void addQueryParamsToRequest(String requestKey, DataTable dataTable) {
        addQueryParams(requestKey, dataTable);
    }

    @And("^user adds Query Params to the request$")
    public void addQueryParamsToRequest(DataTable dataTable) {
        addQueryParams(dataTable);
    }

    @And("^user verifies the response matches \"(.*)\" schema for \"(.*)\" response$")
    public void verifyResponseMatchesSchema(String schemaName, String responseKey) {
        verifyResponseSchema(schemaName, responseKey);
    }

    @And("^user verifies the response matches \"(.*)\" schema$")
    public void verifyResponseMatchesSchema(String schemaName) {
        verifyResponseSchema(schemaName);
    }

    @When("^user verifies state of key-value in response body for \"(.*)\" response$")
    public void verifyPresenceOfKeyValue(String responseKey, DataTable dataTable) {
        verifyKeyValueInResponseBody(dataTable, responseKey);
    }

    @And("^user adds multipart file \"(.*)\" with \"(.*)\" control name$")
    public void addMultipartFileWithControlName(String fileName, String controlName) {
        addMultipartFileToRequest(fileName, controlName);
    }

    @And("^user adds multipart file \"(.*)\" with \"(.*)\" control name to \"(.*)\" request$")
    public void addMultipartFileWithControlName(String fileName, String controlName, String requestKey) {
        addMultipartFileToRequest(controlName, fileName, requestKey);
    }

    @And("^user adds multipart file \"(.*)\" with \"(.*)\" control name and \"(.*)\" MIME type$")
    public void addMultipartFileWithControlNameAndMIMEType(String fileName, String controlName, String mimeType) {
        addMultipartFileToRequestWithMIMEType(fileName, controlName, mimeType);
    }

    @And("^user adds multipart file \"(.*)\" with \"(.*)\" control name and \"(.*)\" MIME type to \"(.*)\" request$")
    public void addMultipartFileWithControlNameAndMIMEType(String fileName, String controlName, String mimeType, String requestKey) {
        addMultipartFileToRequestWithMIMEType(fileName, controlName, mimeType, requestKey);
    }

    @And("^user sets relaxed HTTPS validation$")
    public void setRelaxedHTTPSValidation() {
        setRelaxedHttpsValidation();
    }

    @And("^user sets relaxed HTTPS validation for \"(.*)\" request$")
    public void setRelaxedHTTPSValidation(String requestKey) {
        setRelaxedHttpsValidation(requestKey);
    }

    @And("^user sets url encoding to \"(.*)\"$")
    public void setUrlEncodingTo(boolean encodingStatus) {
        setUrlEncoding(encodingStatus);
    }

    @And("^user sets url encoding to \"(.*)\" for \"(.*)\" request$")
    public void setUrlEncodingTo(boolean encodingStatus, String requestKey) {
        setUrlEncoding(encodingStatus, requestKey);
    }

    @And("^user verifies value \"(.*)\" is found in Json array response for \"(.*)\" key in response body$")
    public void verifyValueInJsonArrayInResponse(String expectedValue, String keyName) {
        verifyValueInJsonArray(keyName, expectedValue);
    }

    @And("^user verifies value \"(.*)\" is found in Json array response for \"(.*)\" key for \"(.*)\" response$")
    public void verifyValueInJsonArrayInResponse(String expectedValue, String keyName, String responseKey) {
        verifyValueInJsonArray(keyName, expectedValue, responseKey);
    }

    @And("^user sets Content-Type as \"(.*)\"$")
    public void userSetsContentType(String contentType) {
        setContentType(contentType);
    }

    @And("^user sets Content-Type as \"(.*)\" for \"(.*)\" request$")
    public void userSetsContentType(String contentType, String keyName) {
        setContentType(contentType, keyName);
    }

    @And("^user verifies the response time taken is less than \"(.*)\" seconds in response body$")
    public void verifyResponseTimeOfResponse(String expectedTime) {
        verifyResponseTime(expectedTime);
    }

    @And("^user verifies the response time taken is less than \"(.*)\" seconds for \"(.*)\" response$")
    public void verifyResponseTimeOfResponse(String expectedTime, String responseKey) {
        verifyResponseTime(expectedTime, responseKey);
    }

    @And("^user verifies value \"(.*)\" is found in Json array response for key with Json Path \"(.*)\" in response$")
    public void verifiesValueInJsonArray(String expectedValue, String jsonPath) {
        verifyValueInJsonArray(jsonPath, expectedValue);
    }

    @And("^user verifies value \"(.*)\" is found in Json array response for key with Json Path \"(.*)\" for \"(.*)\" response$")
    public void verifiesValueInJsonArray(String expectedValue, String jsonPath, String responseKey) {
        verifyValueInJsonArray(jsonPath, expectedValue, responseKey);
    }

    @And("^user verifies the presence of message \"(.*)\" in response body$")
    public void validateMessageInResponseBody(String expectedMessage) {
        verifyPresenceOfMessage(expectedMessage);
    }

    @And("^user verifies the presence of message \"(.*)\" in response body for \"(.*)\" response body$")
    public void validateMessageInResponseBody(String expectedMessage, String responseKey) {
        verifyPresenceOfMessage(expectedMessage, responseKey);
    }

    @And("^user verifies the size of array with key \"(.*)\" is \"(.*)\" in response body$")
    public void verifyArraySizeForKey(String keyName, String size) {
        verifyJsonArraySize(keyName, size);
    }

    @And("^user verifies the size of array with key \"(.*)\" is \"(.*)\" for \"(.*)\" response body$")
    public void verifyArraySizeForKey(String keyName, int size, String responseKey) {
        verifyJsonArraySize(keyName, size, responseKey);
    }


    @Then("^user clears all requests$")
    public void clearAllRequests() {
        clearRequests();
    }

    @Then("^user clears all responses$")
    public void clearAllResponses() {
        clearResponses();
    }

    @Then("^user deletes \"(.*)\" request$")
    public void clearSpecificRequest(String key) {
        clearRequest(key);
    }

    @Then("^user deletes \"(.*)\" response$")
    public void clearSpecificResponse(String key) {
        clearResponse(key);
    }

    @And("^user adds \"(.*)\" body$")
    public void userAddsBodyToRequest(String requestBody) {
        userAddsBody(requestBody);
    }

    @And("^user adds \"(.*)\" body to \"(.*)\" request$")
    public void userAddsBodyToRequest(String methodBody, String keyName) {
        userAddsBody(methodBody, keyName);
    }

    @Then("^user extracts value from \"(.*)\" response$")
    public void userExtractsValueFromFromResponse(String key) {
        extractValue(key);
    }

    @Then("^user adds extracted value of key \"(.*)\" in \"(.*)\" request body$")
    public void userAddsExtractedKeyInBody(String extractedKey, String methodBody) {
        userAddsExtractedValueFromResponseToARequest(extractedKey, methodBody);
    }

    @Then("^user verifies state of key-value in response body$")
    public void userVerifiesStateOfKeyValueInResponseBody(DataTable dataTable) {
        verifyKeyValueInResponseBody(dataTable);
    }

    @And("^user adds Basic Auth with username and password$")
    public void userAddsBasicAuthWithUserAndPassword() {
        addBasicAuth();
    }

    @And("^user adds Bearer Auth with clientID and clientSecret$")
    public void userAddsBearerAuthWithClientAndSecret() {
        addBearerAuth();
    }

    @And("^user adds form parameters$")
    public void userAddsFormParameters(DataTable dataTable) {
        addFormParams(dataTable);
    }

    @And("^user add form parameters to \"(.*)\" request$")
    public void userAddsFormParametersToRequest(String requestKey, DataTable dataTable) {
        addFormParams(requestKey, dataTable);
    }

    @And("^user adds path parameters$")
    public void userAddsPathParameters(DataTable dataTable) {
        addPathParams(dataTable);
    }

    @And("^user add path parameters to \"(.*)\" request$")
    public void userAddPathParametersToRequest(String requestKey, DataTable dataTable) {
        addPathParams(requestKey, dataTable);
    }

    @When("^user add Headers$")
    public void userAddHeaders() {
        // Multiple headers can also be added in the form of a HashMap
        Map<String, String> headers = new HashMap<>();
        headers.put("Key1", "Value");
        headers.put("Key2", "Value");
        headers.put("Key3", "Value");
        addHeadersViaMap(headers);  // Can be used to add the headers to the latest request
        addHeadersViaMap("requestOne", headers);  // Can be used to add the headers to the specified request (Here "requestOne")
    }

    @When("^user add Query Params$")
    public void userAddQueryParams() {
        addQueryParams("Key", "Value");
        addQueryParams("requestOne", "Key", "Value");
    }

    @When("^user add Form Params$")
    public void userAddFormParams() {
        addFormParams("Key", "Value");
        addFormParams("requestOne", "Key", "Value");
    }

    @When("^user add Path Params$")
    public void userAddPathParams() {
        addPathParams("Key", "Value");
        addPathParams("requestOne", "Key", "Value");
    }

    public static String generateAccessToken() {
        String authToken;
        // Implement logic here to generate the access token. Modify the input parameters accordingly
        authToken = EnvironmentSpecificConfiguration.from(SystemEnvironmentVariables.createEnvironmentVariables()).getProperty("accessToken");
        return authToken;
    }
}
