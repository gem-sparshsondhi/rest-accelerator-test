package stepdefinitions;

import common.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.annotations.events.BeforeScenario;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.environment.SystemEnvironmentVariables;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UtilStepDefinition extends CommonFunctions {
    @BeforeScenario
    public void initializeVariables() {
        resetVariables();
    }

    @Given("^user creates a new request named \"(.*)\" request and sets \"(.*)\" as endpoint$")
    public void createNewRequestAndSetEndpoint(String key, String endpoint) {
        createsRequest(key, endpoint);
    }

    @And("^user sends a \"(.*)\" request$")
    public void makesRequest(String method) {
        sendRequest(method);
    }

    @And("^user sends \"(.*)\" request for \"(.*)\" request$")
    public void makesRequest(String method, String requestKey) {
        sendRequest(method, requestKey);
    }

    @Then("^user verifies \"(.*)\" status code$")
    public void verifiesStatusCode(int statusCode) {
        verifyStatusCode(statusCode);
    }

    @Then("^user verifies response code as \"(.*)\" for \"(.*)\" response$")
    public void verifiesStatusCode(int statusCode, String responseKey) {
        verifyStatusCode(statusCode, responseKey);
    }

    @Then("^user adds headers to the request$")
    public void addHeadersToRequest(DataTable dataTable) {
        addHeaders(dataTable);
    }

    @Then("^user adds headers to \"(.*?)\" request$")
    public void addHeadersToRequest(String requestKey, DataTable dt) {
        addHeaders(requestKey, dt);
    }

    @When("^user adds header named \"(.*)\" with value \"(.*)\" to the request$")
    public void addHeadersAsString(String headerName, String headerValue) {
        addHeaders(headerName, headerValue);
    }

    @When("^user adds header named \"(.*)\" with value \"(.*)\" for \"(.*)\" request$")
    public void addHeadersAsString(String requestKey, String headerName, String headerValue) {
        addHeaders(requestKey, headerName, headerValue);
    }

    @When("^user adds headers as map to the request$")
    public void userAddHeaders() {
        // Multiple headers can also be added in the form of a HashMap
        Map<String, String> headers = new HashMap<>();
        headers.put("Key1", "Value");
        headers.put("Key2", "Value");
        headers.put("Key3", "Value");
        addHeadersViaMap(headers);  // Can be used to add the headers to the latest request
        addHeadersViaMap("requestOne", headers);  // Can be used to add the headers to the specified request (Here "requestOne")
    }

    @When("^user adds headers as map for \"(.*)\" request$")
    public void addHeadersAsMap(String requestKey) {
        // Multiple headers can also be added in the form of a HashMap
        Map<String, String> headers = new HashMap<>();
        headers.put("Key1", "Value");
        headers.put("Key2", "Value");
        headers.put("Key3", "Value");
        addHeadersViaMap(requestKey, headers);  // Can be used to add the headers to the specified request (Here "requestOne")
    }

    @When("^user adds Query Param as map for \"(.*)\" request$")
    public void addQueryParamAsMap(String requestKey) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("Key1", "Value");
        queryParams.put("Key2", "Value");
        queryParams.put("Key3", "Value");
        addQueryParamsViaMap(requestKey, queryParams);
    }

    @When("^user adds Query Param as map to the request$")
    public void addQueryParamAsMap() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("Key1", "Value");
        queryParams.put("Key2", "Value");
        queryParams.put("Key3", "Value");
        addQueryParamsViaMap(queryParams);
    }

    @When("^user adds Path Param as map for \"(.*)\" request$")
    public void addPathParamAsMap(String requestKey) {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("Key1", "Value");
        pathParams.put("Key2", "Value");
        pathParams.put("Key3", "Value");
        addPathParamsViaMap(requestKey, pathParams);
    }

    @When("^user adds Path Param as map to the request$")
    public void addsPathParamAsMap() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("Key1", "Value");
        pathParams.put("Key2", "Value");
        pathParams.put("Key3", "Value");
        addPathParamsViaMap(pathParams);
    }

    @When("^user adds Form Param as map for \"(.*)\" request$")
    public void addFormParamAsMap(String requestKey) {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("Key1", "Value");
        formParams.put("Key2", "Value");
        formParams.put("Key3", "Value");
        addFormParamsViaMap(requestKey, formParams);
    }

    @When("^user adds Form Param as map to the request$")
    public void addsFormParamAsMap() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("Key1", "Value");
        formParams.put("Key2", "Value");
        formParams.put("Key3", "Value");
        addFormParamsViaMap(formParams);
    }

    @And("^user adds Query Params to \"(.*)\" request$")
    public void addQueryParamsToRequest(String requestKey, DataTable dataTable) {
        addQueryParams(requestKey, dataTable);
    }

    @And("^user adds Query Params to the request$")
    public void addQueryParamsToRequest(DataTable dataTable) {
        addQueryParams(dataTable);
    }

    @When("^user adds Query Parameter named \"(.*)\" with value \"(.*)\" to the request$")
    public void addQueryParam(String key, String value) {
        addQueryParams(key, value);
    }

    @And("^user adds form parameters$")
    public void addsFormParameters(DataTable dataTable) {
        addFormParams(dataTable);
    }

    @And("^user adds form parameters to \"(.*)\" request$")
    public void addFormParameters(String requestKey, DataTable dataTable) {
        addFormParams(requestKey, dataTable);
    }

    @And("^user adds path parameters$")
    public void addsPathParameters(DataTable dataTable) {
        addPathParams(dataTable);
    }

    @And("^user adds path parameters to \"(.*)\" request$")
    public void addPathParameters(String requestKey, DataTable dataTable) {
        addPathParams(requestKey, dataTable);
    }

    @When("^user adds Query Parameter named \"(.*)\" with value \"(.*)\" to \"(.*)\" request$")
    public void addQueryParam(String key, String value, String requestKey) {
        addQueryParams(requestKey, key, value);
    }

    @When("^user adds Path Parameter named \"(.*)\" with value \"(.*)\" to \"(.*)\" request$")
    public void addPathParam(String key, String value, String requestKey) {
        addPathParams(requestKey, key, value);
    }

    @When("^user adds Form Parameter named \"(.*)\" with value \"(.*)\" to \"(.*)\" request$")
    public void addFormParam(String key, String value, String requestKey) {
        addFormParams(requestKey, key, value);
    }

    @When("^user adds Form Parameter named \"(.*)\" with value \"(.*)\" to the request$")
    public void addFormParam(String key, String value) {
        addFormParams(key, value);
    }

    @When("^user adds Path Parameter named \"(.*)\" with value \"(.*)\" to the request$")
    public void addPathParam(String key, String value) {
        addPathParams(key, value);
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
    public void setsContentType(String contentType) {
        setContentType(contentType);
    }

    @And("^user sets Content-Type as \"(.*)\" for \"(.*)\" request$")
    public void setsContentType(String contentType, String keyName) {
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
    public void addsBodyToRequest(String requestBody) {
        addBody(requestBody);
    }

    @And("^user adds \"(.*)\" body to \"(.*)\" request$")
    public void addsBodyToRequest(String methodBody, String keyName) {
        addBody(methodBody, keyName);
    }

    @Then("^user extracts \"(.*)\" from response$")
    public void extractsValueFromFromResponse(String key) {
        extractValue(key);
    }

    @When("^user extracts \"(.*)\" from \"(.*)\" response")
    public void extractsFromResponse(String extractedKey, String requestKey) {
        extractValue(extractedKey, requestKey);
    }

    @When("^user extracts the following keys from response")
    public void extractsFromResponse(DataTable dt) {
        extractValues(dt);
    }

    @When("^user extracts the following keys from \"(.*)\" response")
    public void extractsFromResponse(DataTable dataTable, String requestKey) {
        extractValues(dataTable, requestKey);
    }

    @Then("^user adds extracted value of key \"(.*)\" at \"(.*?)\" path in \"(.*)\" request body$")
    public void addsExtractedKeyInBody(String extractedKey, String pathToSet, String methodBody) {
        addExtractedValueToRequest(extractedKey, pathToSet, methodBody);
    }

    @When("^user adds extracted value of key \"(.*)\" at \"(.*)\" path in \"(.*)\" request body of \"(.*)\" request$")
    public void addsExtractedValueInRequestBody(String extractedKey, String pathToSet, String methodBody, String requestKey) {
        addExtractedValueToRequest(extractedKey, pathToSet, methodBody, requestKey);

    }

    @Then("^user verifies state of key-value in response body$")
    public void verifiesStateOfKeyValueInResponseBody(DataTable dataTable) {
        verifyKeyValueInResponseBody(dataTable);
    }

    @And("^user adds Basic Auth with username and password$")
    public void addsBasicAuth() {
        addBasicAuth();
        addBasicAuth("");
    }

    @And("^user adds Bearer Auth with clientID and clientSecret$")
    public void addsBearerAuth() {
        addBearerAuth();
        addBearerAuth("");
    }


    @When("^user compares response \"(.*?)\" with \"(.*?)\" response using JSON Comparator$")
    public void performJsonComparison(String responseKey1, String responseKey2) throws IOException {
        performComparison(responseKey1, responseKey2);
    }

    public static String generateBearerToken() {
        String authToken;
        // Implement logic here to generate the access token. Modify the input parameters accordingly
        authToken = EnvironmentSpecificConfiguration.from(SystemEnvironmentVariables.createEnvironmentVariables()).getProperty("accessToken");
        return authToken;
    }
}
