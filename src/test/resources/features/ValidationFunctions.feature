@RestTests @validationsTest
Feature: RestAssured CommonFunction Tests

  Background: Get the response
    Given user hits local server

  @validationsTest
  Scenario: Verify all response validation functions
    When User verifies value "donut" is found in response for "type" key
    When User verifies value "Glazed" is found in response for "topping[1].type" key
    And User verifies the response matches "testSchema" schema
    And User verifies the size of array with key "topping" is "7"
    And User verifies the size of array with key "batters.batter" is "4"
    And User verifies the size of array with key "id" is "0"
    And User verifies the response time taken is less than "7" seconds
    And User verifies the response time taken is less than "3" seconds
    And User verifies the presence of message "donut" in response body
    And User verifies the presence of message "Devil's Food" in response body
    And user verifies response code as "200"
    And User verifies value "600" is found in Json array response for "prices" key
    And User verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices"

  @expectedFails
  Scenario: Fail scenario for verifyKeyAndValuePresence function
    When User verifies value "donuts" is found in response for "type" key

  @expectedFails
  Scenario: Fail scenario for verifyKeyAndValuePresence function with json path
    When User verifies value "Glazed1" is found in response for "topping[1].type" key

  @expectedFails
  Scenario: Fail scenario for verifyResponseSchema function
    And User verifies the response matches "badSchema" schema

  @expectedFails
  Scenario: Fail scenario for verifyJsonArraySize function
    And User verifies the size of array with key "toppingss" is "7"

  @expectedFails
  Scenario: Fail scenario for verifyJsonArraySize function with invalid size
    And User verifies the size of array with key "topping" is "700"

  @expectedFails
  Scenario: Fail scenario for verifyJsonArraySize function with invalid format
    And User verifies the size of array with key "toppingss" is "abc"

  @expectedFails
  Scenario: Fail scenario for verifyResponseTime function
    And User verifies the response time taken is less than "0.1" seconds

  @expectedFails
  Scenario: Fail scenario for messageValidation function
    And User verifies the presence of message "technician" in response body

  @expectedFails
  Scenario: Fail scenario for verifyStatusCode function
    And user verifies response code as "204"

  @expectedFails
  Scenario: Fail scenario for verifyValueInJsonArray function
    And User verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2"
