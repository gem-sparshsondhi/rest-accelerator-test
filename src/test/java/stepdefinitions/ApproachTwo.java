package stepdefinitions;

import Common.CommonFunctions;
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
        userCreatesRequest("getSingleUser", "read");
    }

    @When("^user hit a GET request$")
    public void userHitAGETRequest() {
        userHitsRequest("GET");
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
        userCreatesRequest("PostData", "create");
        addHeaders("PostData", "Accept", "*/*");
        userAddsBody("POSTReqBody", "PostData");
    }

    @When("user extracts key from response")
    public void userExtractsKeyFromResponse() {
        extractValue("data.email");
    }

    @Then("user adds extracted value from response to request body")
    public void userAddsExtractedValueFromResponseToRequestBody() {
        addExtractedValueFromResponseToARequest("email", "email", "POSTReqBody");
    }

    @When("user hit a POST request and validates status code")
    public void userHitAPOSTRequestAndValidatesStatusCode() {
        userHitsRequest("POST", "PostData");
        verifyStatusCode(201);
    }


    @Given("user creates a new request with body")
    public void userCreatesANewRequestWithBody() {
        userCreatesRequest("PostNewData", "create");
        addHeaders("PostNewData", "Accept", "*/*");
        userAddsBody("POSTReqBody", "PostNewData");
    }

    @And("^user hit a POST request$")
    public void userHitAPOSTRequest() {
        userHitsRequest("POST", "PostNewData");
    }

    @Then("^user validates status code for POST request$")
    public void userValidatesStatusCodeForPOSTRequest() {
        verifyStatusCode(201);
    }

    @Given("user creates a PUT request with body")
    public void userCreatesAPUTRequestWithBody() {
        userCreatesRequest("PutData","update");
    }

    @Then("^user hits PUT request and verifies status code$")
    public void userValidatesStatusCodeForPUTRequest() {
        userHitsRequest("PUT", "PutData");
        verifyStatusCode(200, "PutData");
    }
}
