@SampleRestTests
Feature: Sample Feature file to demonstrate how a rest suite would look when using this accelerator

  @Authorization
  Scenario:
    When user adds Basic Auth with username and password
    When user adds Bearer Auth with clientID and clientSecret

  @Requests
    Scenario:
    #single request
    Given user creates a new request named "requestOne" request and sets "custom" as endpoint
    When user sets Content-Type as "application/json"
    When user sets relaxed HTTPS validation
    When user sets url encoding to "false"
    When user adds headers to the request
      | Header Name | Header Value |
      | header1     | value1       |
      | header2     | value2       |
    When user adds Query Params to the request
      | Parameter Key | Parameter Value |
      | Key1          | Val1            |
    When user adds form parameters
      | Parameter Key | Parameter Key |
      | key           | val           |
    When user adds path parameters
      | Parameter Key | Parameter Key |
      | key           | val           |
    When user add Headers
    When user add Query Params
    When user add Form Params
    When user add Path Params
    When user adds "POSTReqBody" body
    When user makes a "post" request

    #multiple requests(1)
    Given user creates a new request named "requestTwo" request and sets "custom" as endpoint
    When user sets Content-Type as "application/json" for "requestTwo" request
    When user sets relaxed HTTPS validation for "requestTwo" request
    When user sets url encoding to "false" for "requestTwo" request
    When user adds headers to "requestTwo" request
      | Header Name | Header Value |
      | header1     | value1       |
    When user add form parameters to "PostData" request
      | Parameter Key | Parameter Key |
      | key           | val           |
    When user add Query Params to "requestTwo" request
      | Parameter Key | Parameter Value |
      | Key1          | Val1            |
    When user add path parameters to "PostData" request
      | Parameter Key | Parameter Key |
      | key           | val           |
    When user makes "get" request for "requestTwo" request
    When user extracts value from "data.email" response
    When user adds extracted value of key "data.email" in "POSTReqBody" request body

   #multiple requests(2)
    Given user creates a new request named "requestThree" request and sets "custom" as endpoint
    When user sets Content-Type as "multipart/form-data"
    When user adds multipart file "SampleMultipartFile.txt" with "file" control name
    When user adds multipart file "SampleMultipartFile.txt" with "file" control name and "text/html" MIME type
    When user adds headers to the request
      | Header Name | Header Value |
      | header1     | value1       |
    When user makes a "GET" request
    Then user verifies "200" status code
    When user creates a new request named "requestFour" request and sets "custom" as endpoint
    When user sets Content-Type as "multipart/json" for "requestFour" request
    When user adds headers to "requestFour" request
      | Header Name | Header Value |
      | header1     | value1       |
    When user adds Query Params to the request
      | Parameter Key | Parameter Value |
      | Param1        | ParamVal1       |
      | Param2        | ParamVal2       |
    When user adds multipart file "SampleMultipartFile.txt" with "file" control name to "requestFour" request
    When user adds multipart file "SampleMultipartFile.txt" with "file" control name and "text/html" MIME type to "requestFour" request
    When user adds "requestbodysample" body to "requestFour" request
    When user makes a "patch" request

  @Validations
    #single
  Scenario:
    Then user verifies state of key-value in response body
      | Key             | Value  |
      | topping[1].type | Glazed |
      | type            | donut  |
    Then user verifies the response matches "testSchema" schema
    Then user verifies the size of array with key "topping" is "7" in response body
    Then user verifies the response time taken is less than "7" seconds in response body
    Then user verifies the presence of message "donut" in response body
    Then user verifies value "600" is found in Json array response for "prices" key in response body
    Then user verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices" in response
    Then user verifies "200" status code

    #multiple
    Then user verifies state of key-value in response body for "requestTwo" response
      | Key             | Value  |
      | type            | donut  |
      | topping[1].type | Glazed |
    Then user verifies the response matches "testSchema" schema for "requestTwo" response
    Then user verifies the size of array with key "topping" is "7" for "requestTwo" response body
    Then user verifies the size of array with key "id" is "0" for "requestTwo" response body
    Then user verifies the response time taken is less than "7" seconds for "requestTwo" response
    Then user verifies the presence of message "donut" in response body for "requestTwo" response body
    Then user verifies response code as "200" for "requestTwo" response
    Then user verifies value "600" is found in Json array response for "prices" key for "requestTwo" response
    Then user verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices" for "requestTwo" response

  @Clear
  Scenario:
    When user deletes "DeleteData" request
    When user deletes "DeleteData" response
    When user clears all requests
    When user clears all responses