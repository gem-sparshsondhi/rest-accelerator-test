package stepdefinitions;

import common.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.annotations.events.BeforeScenario;

public class ApproachTwo extends CommonFunctions {

    @BeforeScenario
    public void initializeVariables() {
        resetVariables();
    }

    @Given("^user creates a request$")
    public void createRequest() {
        createsRequest("GetUserDetails", "read");
    }

    @When("^user sends a GET request$")
    public void sendGETRequest() {
        sendRequest("GET");
    }

    @When("^user does status code validation$")
    public void doStatusCodeValidation() {
        verifyStatusCode(200);
    }

    @Then("^user does the data validations$")
    public void doDataValidations(DataTable dataTable) {
        verifyKeyValueInResponseBody(dataTable);
    }

    @Given("^user creates a request with body$")
    public void createRequestWithBody() {
        createsRequest("CreateUser", "create");
        addHeaders("CreateUser", "Accept", "*/*");
        addsBody("POSTReqBody", "CreateUser");
    }

    @When("user extracts key from response")
    public void extractKeyFromResponse() {
        extractValue("data.email");
    }

    @Then("user adds extracted value from response to request body")
    public void addExtractedValueToRequestBody() {
        addExtractedValueToRequest("email", "email", "POSTReqBody");
    }

    @When("user sends a POST request and validates status code")
    public void sendPostRequestAndValidateStatusCode() {
        sendRequest("POST", "CreateUser");
        verifyStatusCode(201);
    }

    @Given("user creates a new request with body")
    public void createNewRequestWithBody() {
        createsRequest("CreateUser", "create");
        addHeaders("CreateUser", "Accept", "*/*");
        addsBody("POSTReqBody", "CreateUser");
    }

    @And("^user sends a POST request$")
    public void sendPostRequest() {
        sendRequest("POST", "CreateUser");
    }

    @Then("^user validates status code for POST request$")
    public void validateStatusCode() {
        verifyStatusCode(201);
    }

    @Given("user creates a PUT request with body")
    public void userCreatesAPUTRequestWithBody() {
        createsRequest("UpdateUser", "update");
    }

    @Then("^user sends PUT request and verifies status code$")
    public void userValidatesStatusCodeForPUTRequest() {
        sendRequest("PUT", "UpdateUser");
        verifyStatusCode(200, "UpdateUser");
    }
}
