@Demo
Feature: Sample Simplified Scenarios

  @SimpleScenario1
  Scenario: Create Client Account from other client's data (Simplified)
    Given user gets first client's data
    Then user performs status code and data validations
      | Key             | operation    | Value                   |
      | data.first_name | equals       | Janet                   |
      | data.first_name | not equals   | Janeta                  |
      | data.email      | contains     | janet.weaver@reqres.in  |
      | data.email      | not contains | janeta.weaver@reqres.in |
    Then user extracts email from response
    Given user creates a new client account


  @SimpleScenario2
  Scenario: Create and update a Client Account (Simplified)
    Given user creates a new Client Account
    When user prepares updates for the Client Account
    Then user updates the Client Account











