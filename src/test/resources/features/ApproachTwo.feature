@Demo2
Feature: Extra User friendly scenarios

  @newScenario
  Scenario:GET Request
    Given user creates a request
    When user hit a GET request
    Then user does status code validation
    And user does the data validations
      | Key             | operation    | Value                   |
      | data.first_name | equals       | Janet                   |
      | data.first_name | not equals   | Janeta                  |
      | data.email      | contains     | janet.weaver@reqres.in  |
      | data.email      | not contains | janeta.weaver@reqres.in |
    When user extracts key from response
    Given user creates a request with body
    Then user adds extracted value from response to request body
    When user hit a POST request and validates status code


  @newScenario2
  Scenario: Modify Request body and make two requests
    Given user creates a new request with body
    When user hit a POST request
    And user validates status code for POST request
    Given user creates a PUT request with body
    When user hits PUT request and verifies status code











