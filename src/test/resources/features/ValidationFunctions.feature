@RestTests @validationsTest
Feature: RestAssured CommonFunction Validation Tests. Batch functionality verification

  Background: Get the response
    Given User creates a new request
    And User sets base URI as "http://localhost:3000"
    And User sets path as "/items"
    Then User makes a "get" request

  @validationsTest
  Scenario: Verify all response validation functions
    When User verifies value "donut" is found in response for "type" key for latest response
    When User verifies value "Glazed" is found in response for "topping[1].type" path for latest response
    And User verifies the response matches "testSchema" schema for latest response
    And User verifies the size of array with key "topping" is "7" in latest response
    And User verifies the size of array with key "batters.batter" is "4" in latest response
    And User verifies the size of array with key "id" is "0" in latest response
    And User verifies the response time taken is less than "7" seconds in latest response
    And User verifies the response time taken is less than "3" seconds in latest response
    And User verifies the presence of message "donut" in latest response body
    And User verifies the presence of message "Devil's Food" in latest response body
    And User verifies response code as "200" for latest response
    And User verifies value "600" is found in Json array response for "prices" key in latest response
    And User verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices" in latest response

  @expectedFails
  Scenario: Fail scenario for verifyKeyAndValuePresence function
    When User verifies value "donuts" is found in response for "type" key for latest response

  @expectedFails
  Scenario: Fail scenario for verifyKeyAndValuePresence function with invalid key
    When User verifies value "donuts" is found in response for "blah" key for latest response

  @expectedFails
  Scenario: Fail scenario for verifyKeyAndValuePresence function with json path
    When User verifies value "Glazed1" is found in response for "topping[1].type" path for latest response

  @expectedFails
  Scenario: Fail scenario for verifyKeyAndValuePresence function with invalid json path
    When User verifies value "Glazed1" is found in response for "topping[1].type.blah" path for latest response

  @expectedFails
  Scenario: Fail scenario for verifyResponseSchema function
    And User verifies the response matches "badSchema" schema for latest response

  @expectedFails
  Scenario: Fail scenario for verifyResponseSchema function with non-existent schema
    And User verifies the response matches "placeholderSchema" schema for latest response

  @expectedFails
  Scenario: Fail scenario for verifyResponseSchema function with invalid schema
    And User verifies the response matches "invalidSchema" schema for latest response

  @expectedFails
  Scenario: Fail scenario for verifyJsonArraySize function
    And User verifies the size of array with key "toppingss" is "7" in latest response

  @expectedFails
  Scenario: Fail scenario for verifyJsonArraySize function with invalid size
    And User verifies the size of array with key "topping" is "700" in latest response

  @expectedFails
  Scenario: Fail scenario for verifyJsonArraySize function with invalid format
    And User verifies the size of array with key "toppingss" is "abc" in latest response

  @expectedFails
  Scenario: Fail scenario for verifyResponseTime function
    And User verifies the response time taken is less than "0.001" seconds in latest response

  @expectedFails
  Scenario: Fail scenario for verifyJsonArraySize function with invalid format
    And User verifies the response time taken is less than "agasa" seconds in latest response

  @expectedFails
  Scenario: Fail scenario for messageValidation function
    And User verifies the presence of message "technician" in latest response body

  @expectedFails
  Scenario: Fail scenario for verifyStatusCode function
    And User verifies response code as "204" for latest response

  @expectedFails
  Scenario: Fail scenario for verifyValueInJsonArray function
    And User verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2" in latest response

  @expectedFails
  Scenario: Fail scenario for verifyValueInJsonArray function with invalid value
    And User verifies value "6000" is found in Json array response for key with Json Path "custom.value1.value2.prices" in latest response
