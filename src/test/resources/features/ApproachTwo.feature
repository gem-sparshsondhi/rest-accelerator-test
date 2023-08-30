@Demo2
Feature: Extra User friendly scenarios

  @newScenario
  Scenario: Get data and extract a value
    Given user creates a request
    When user hit a GET request
    Then user does status code validation
    And user does the data validations
      | Key             | operation    | Value                   |
      | data.first_name | equals       | Janet                   |
      | data.first_name | not equals   | Janeta                  |
      | data.email      | contains     | janet.weaver@reqres.in  |
      | data.email      | not contains | janeta.weaver@reqres.in |

  @newScenario2
  Scenario: Modify Request body and make two requests
    Given user creates a request with body
    When user hit a POST request
    Then user extracts value from response
    And user validates status code for POST request
    Given user creates another request with premade body after adding extracted data
    When user hits PUT request
    Then user validates status code for PUT request











