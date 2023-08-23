@Demo
Feature: Demo

  @newScenario
  Scenario: Extra User friendly scenario
    Given user creates a request
    And user hit a GET request
    When user does status code validation
    Then user does the data validations
      | Key             | operation    | Value                   |
      | data.first_name | equals       | Janet                   |
      | data.first_name | not equals   | Janeta                  |
      | data.email      | contains     | janet.weaver@reqres.in  |
      | data.email      | not contains | janeta.weaver@reqres.in |
    When user extracts value from response

  @newScenario2
  Scenario: Extra User friendly scenario
    Given user creates a request with body
    When user adds extracted key-value in request body
    When user hit a POST request
    Then user validates status code for POST request
    Given user creates a new request with body
    When user hits PUT request
    Then user validates status code for PUT request









