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
    public void userCreatesAUser() {
        createsRequest("getSingleUser", "read");
    }

    @When("^user hit a GET request$")
    public void userHitAGETRequest() {
        hitsRequest("GET");
    }

    @When("^user does status code validation$")
    public void userDoesStatusCodeValidation() {
        verifyStatusCode(200);
    }

    @Then("^user does the data validations$")
    public void userDoesTheDataValidations(DataTable dataTable) {
        verifyKeyValueInResponseBody(dataTable);
    }


    @Given("^user creates a request with body$")
    public void userCreatesARequestWithBody() {
        createsRequest("PostData", "create");
        addHeaders("PostData", "Accept", "*/*");
        addsBody("POSTReqBody", "PostData");
    }

    @When("user extracts key from response")
    public void userExtractsKeyFromResponse() {
        extractValue("data.email");
    }

    @Then("user adds extracted value from response to request body")
    public void userAddsExtractedValueFromResponseToRequestBody() {
        addExtractedValueToRequest("email", "email", "POSTReqBody");
    }

    @When("user hit a POST request and validates status code")
    public void userHitAPOSTRequestAndValidatesStatusCode() {
        hitsRequest("POST", "PostData");
        verifyStatusCode(201);
    }


    @Given("user creates a new request with body")
    public void userCreatesANewRequestWithBody() {
        createsRequest("PostNewData", "create");
        addHeaders("PostNewData", "Accept", "*/*");
        addsBody("POSTReqBody", "PostNewData");
    }

    @And("^user hit a POST request$")
    public void userHitAPOSTRequest() {
        hitsRequest("POST", "PostNewData");
    }

    @Then("^user validates status code for POST request$")
    public void userValidatesStatusCodeForPOSTRequest() {
        verifyStatusCode(201);
    }

    @Given("user creates a PUT request with body")
    public void userCreatesAPUTRequestWithBody() {
        createsRequest("PutData", "update");
    }

    @Then("^user hits PUT request and verifies status code$")
    public void userValidatesStatusCodeForPUTRequest() {
        hitsRequest("PUT", "PutData");
        verifyStatusCode(200, "PutData");
    }
}
