@RestTests @RequestTest
Feature: RestAssured CommonFunction Request Creation Tests. Batch functionality verification

  @RequestTest
  Scenario: Verify all request creation functions
    Given User creates a new request
    And User sets base URI as "http://localhost:3000"
    And User sets path as "/items"
    And User sets Content-Type as "application/json"
    And User sets relaxed HTTPS validation
    And User sets url encoding to "false"
    And User adds headers
      | Name    | Value  |
      | header1 | value1 |
    Then User makes a "get" request

    When User creates a new request
    And User sets base URI as "http://localhost:3000"
    And User sets path as "/items"
    And User sets Content-Type as "application/json"
    And User sets relaxed HTTPS validation
    And User sets url encoding to "false"
    And User adds headers
      | Name    | Value  |
      | header1 | value1 |
    And User adds Query Parameters
      | Name   | Value     |
      | Param1 | ParamVal1 |
      | Param2 | ParamVal2 |
    And user sets request body from "requestbodysample" file
    Then User makes a "patch" request

    #below is to return the API response data back to normal
    When User creates a new request
    And User sets base URI as "http://localhost:3000"
    And User sets path as "/items"
    And User sets Content-Type as "application/json"
    And User sets relaxed HTTPS validation
    And User sets url encoding to "false"
    And User adds headers
      | Name    | Value  |
      | header1 | value1 |
    And User adds multiple Query Parameters
      | Name1     | Name2     | Name3     | Name4     |
      | ParamVal1 | ParamVal2 | ParamVal3 | ParamVal4 |
    And user sets request body from "ReceivedResponse.json" file
    Then User makes a "post" request