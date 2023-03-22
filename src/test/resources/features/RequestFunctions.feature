@RestTests
Feature: RestAssured CommonFunction Tests

  @RequestTest
  Scenario: Verify all request creation functions
    Given User creates a new request
    And User sets base URI as "https://localhost:8080"
    And User sets path as "items"
    And User sets Content-Type as "application/json"
    And user sets relaxed HTTPS validation
    And user sets url encoding to "false"
    And User adds headers
      | Name    | Value  |
      | header1 | value1 |
    And User adds Query Parameters
      | Name   | Value     |
      | Param1 | ParamVal1 |
    Then user makes a "get" request