@SampleRestTests
Feature: Sample Feature file to demonstrate how a rest suite would look when using this accelerator

  @singleRequestAndValidations
  Scenario: Single request with validations
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

  @MultiRequestAlternateValidations
  Scenario: Multiple requests with alternating validations
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
    Then User creates a new request
    And User sets base URI as "http://localhost:3000"
    And User sets path as "/items"
    And User sets Content-Type as "application/json"
    And User sets relaxed HTTPS validation
    And User sets url encoding to "false"
    And User adds headers
      | Name    | Value  |
      | header1 | value1 |
    Then User makes a "get" request
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

  @MultiRequestDetachedValidations
  Scenario: Multiple requests with validations on both done after the requests were made
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
    Then User makes a "get" request
    When User verifies value "donut" is found in response for "type" key for Response number "1"
    When User verifies value "Glazed" is found in response for "topping[1].type" path for Response number "1"
    And User verifies the response matches "testSchema" schema for Response number "1"
    And User verifies the size of array with key "topping" is "7" for Response number "1"
    And User verifies the size of array with key "batters.batter" is "4" for Response number "1"
    And User verifies the size of array with key "id" is "0" for Response number "1"
    And User verifies the response time taken is less than "7" seconds for Response number "1"
    And User verifies the response time taken is less than "3" seconds for Response number "1"
    And User verifies the presence of message "donut" in response body for Response number "1"
    And User verifies the presence of message "Devil's Food" in response body for Response number "1"
    And User verifies response code as "200" for Response number "1"
    And User verifies value "600" is found in Json array response for "prices" key for Response number "1"
    And User verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices" for Response number "1"
    When User verifies value "donut" is found in response for "type" key for Response number "2"
    When User verifies value "Glazed" is found in response for "topping[1].type" path for Response number "2"
    And User verifies the response matches "testSchema" schema for Response number "2"
    And User verifies the size of array with key "topping" is "7" for Response number "2"
    And User verifies the size of array with key "batters.batter" is "4" for Response number "2"
    And User verifies the size of array with key "id" is "0" for Response number "2"
    And User verifies the response time taken is less than "7" seconds for Response number "2"
    And User verifies the response time taken is less than "3" seconds for Response number "2"
    And User verifies the presence of message "donut" in response body for Response number "2"
    And User verifies the presence of message "Devil's Food" in response body for Response number "2"
    And User verifies response code as "200" for Response number "2"
    And User verifies value "600" is found in Json array response for "prices" key for Response number "2"
    And User verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices" for Response number "2"

  @MultiRequestSingleValidations
  Scenario: Multiple requests with second only validation
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
    Then User creates a new request
    And User sets base URI as "http://localhost:3000"
    And User sets path as "/items"
    And User sets Content-Type as "application/json"
    And User sets relaxed HTTPS validation
    And User sets url encoding to "false"
    And User adds headers
      | Name    | Value  |
      | header1 | value1 |
    Then User makes a "get" request
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

  @MultiRequestDetachedValidationsInverted
  Scenario: Multiple requests with validations on both done after the requests were made
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
    Then User makes a "get" request
    When User verifies value "donut" is found in response for "type" key for Response number "2"
    When User verifies value "Glazed" is found in response for "topping[1].type" path for Response number "2"
    And User verifies the response matches "testSchema" schema for Response number "2"
    And User verifies the size of array with key "topping" is "7" for Response number "2"
    And User verifies the size of array with key "batters.batter" is "4" for Response number "2"
    And User verifies the size of array with key "id" is "0" for Response number "2"
    And User verifies the response time taken is less than "7" seconds for Response number "2"
    And User verifies the response time taken is less than "3" seconds for Response number "2"
    And User verifies the presence of message "donut" in response body for Response number "2"
    And User verifies the presence of message "Devil's Food" in response body for Response number "2"
    And User verifies response code as "200" for Response number "2"
    And User verifies value "600" is found in Json array response for "prices" key for Response number "2"
    And User verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices" for Response number "2"
    When User verifies value "donut" is found in response for "type" key for Response number "1"
    When User verifies value "Glazed" is found in response for "topping[1].type" path for Response number "1"
    And User verifies the response matches "testSchema" schema for Response number "1"
    And User verifies the size of array with key "topping" is "7" for Response number "1"
    And User verifies the size of array with key "batters.batter" is "4" for Response number "1"
    And User verifies the size of array with key "id" is "0" for Response number "1"
    And User verifies the response time taken is less than "7" seconds for Response number "1"
    And User verifies the response time taken is less than "3" seconds for Response number "1"
    And User verifies the presence of message "donut" in response body for Response number "1"
    And User verifies the presence of message "Devil's Food" in response body for Response number "1"
    And User verifies response code as "200" for Response number "1"
    And User verifies value "600" is found in Json array response for "prices" key for Response number "1"
    And User verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices" for Response number "1"