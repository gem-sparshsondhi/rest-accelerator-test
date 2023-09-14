package stepdefinitions;

import common.CommonFunctions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.annotations.events.BeforeScenario;

public class ApproachTwo extends CommonFunctions {

    @BeforeScenario
    public void initializeVariables() {
        resetVariables();
    }

    @Given("^user gets first client's data$")
    public void createRequest() {
        createsRequest("GetClientDetails", "read");
        sendRequest("GET");
    }

    @Then("^user performs status code and data validations$")
    public void doDataValidations(DataTable dataTable) {
        verifyStatusCode(200);
        verifyKeyValueInResponseBody(dataTable);
    }

    @Given("^user creates a new client account$")
    public void createRequestWithBody() {
        createsRequest("CreateClientAccount", "create");
        addHeaders("CreateClientAccount", "Accept", "*/*");
        addBody("POSTReqBody", "CreateClientAccount");
        addExtractedValueToRequest("email", "user[0].email", "POSTReqBody");
        sendRequest("POST", "CreateClientAccount");
        verifyStatusCode(201);
    }

    @When("^user extracts email from response$")
    public void extractKeyFromResponse() {
        extractValue("data.email");
    }

    @Given("^user creates a new Client Account$")
    public void createNewRequestWithBody() {
        createsRequest("CreateClientAccount", "create");
        addHeaders("CreateClientAccount", "Accept", "*/*");
        addBody("POSTReqBody", "CreateClientAccount");
        sendRequest("POST", "CreateClientAccount");
        verifyStatusCode(201);
    }

    @Given("^user prepares updates for the Client Account$")
    public void userCreatesAPUTRequestWithBody() {
        createsRequest("UpdateClientAccount", "update");
    }

    @Then("^user updates the Client Account$")
    public void userValidatesStatusCodeForPUTRequest() {
        sendRequest("PUT", "UpdateClientAccount");
        verifyStatusCode(200, "UpdateClientAccount");
    }
}
