@Demo
Feature: Sample Simplified Scenarios

  @SimpleScenario1
  Scenario: Create User from other user's data
    Given user creates a request
    When user sends a GET request
    Then user does status code validation
    And user does the data validations
      | Key             | operation    | Value                   |
      | data.first_name | equals       | Janet                   |
      | data.first_name | not equals   | Janeta                  |
      | data.email      | contains     | janet.weaver@reqres.in  |
      | data.email      | not contains | janeta.weaver@reqres.in |
    Then user extracts key from response
    Given user creates a request with body
    Then user adds extracted value from response to request body
    When user sends a POST request and validates status code


  @SimpleScenario2
  Scenario: Create and update a user
    Given user creates a new request with body
    When user sends a POST request
    And user validates status code for POST request
    Given user creates a PUT request with body
    When user sends PUT request and verifies status code











