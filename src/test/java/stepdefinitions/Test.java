package stepdefinitions;

import Common.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Test extends CommonFunctions {

    @Given("user creates a request")
    public void userCreatesAUser() {
        userCreatesRequest("getSingleUser", "read");
    }

    @When("user hit a GET request")
    public void userHitAGETRequest() {
        userHitsRequest("GET");
    }

    @When("user does status code validation")
    public void userDoesStatusCodeValidation() {
        verifyStatusCode(200);
    }

    @Then("user does the data validations")
    public void userDoesTheDataValidations(DataTable dataTable) {
        verifyKeyValueInResponseBody(dataTable);
    }

    @Then("user deletes specific request")
    public void userDeletesRequest() {
        clearRequest("DeleteData");
    }


    @Then("user deletes specific response")
    public void userDeletesResponse() {
        clearResponse("DeleteData");

    }


    @When("user extracts value from response")
    public void userExtractsValueFromResponse() {
        extractValue("data.email");
    }

    @When("user adds extracted key-value in request body")
    public void userAddsExtractedKeyValueInRequestBody() {
        userAddsExtractedValueFromResponseToARequest("data.email", "POSTReqBody");
    }

    @Given("user creates a request with body")
    public void userCreatesARequestWithBody() {
        userCreatesRequest("PostData", "create");
        addHeaders("PostData", "Accept", "*/*");
        userAddsBody("POSTReqBody","PostData");
    }

    @And("user hit a POST request")
    public void userHitAPOSTRequest() {
        userHitsRequest("POST", "PostData");
    }


    @Then("user validates status code for POST request")
    public void userValidatesStatusCodeForPOSTRequest() {
        verifyStatusCode(201);
    }

    @Given("user creates a new request with body")
    public void userCreatesANewRequestWithBody() {
        userCreatesRequest("PutData", "update");
        userAddsBody("PUTReqBody","PutData");
    }

    @When("user hits PUT request")
    public void userHitsPUTRequest() {
        userHitsRequest("PUT", "PutData");
    }

    @Then("user validates status code for PUT request")
    public void userValidatesStatusCodeForPUTRequest() {
        verifyStatusCode(200,"PutData");
    }
}
