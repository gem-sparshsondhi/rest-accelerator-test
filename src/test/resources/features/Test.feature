@SampleRestTests
Feature: Sample Feature file to demonstrate all the accelerator functions

  @Authorization
  Scenario: Auth functions
    When user adds Basic Auth with username and password
    When user adds Bearer Auth with clientID and clientSecret


 #---------------------------------------------single request-------------------------------------------
  @Requests
  Scenario: Request Creation functions
    Given user creates a new request named "requestOne" and sets "custom" as endpoint
    When user sets Content-Type as "application/json"
    When user sets relaxed HTTPS validation
    When user sets url encoding to "false"
#    Headers
    When user adds header named "header1" with value "value1" to the request
    When user adds headers to the request
      | Header Name | Header Value |
      | header1     | value1       |
      | header2     | value2       |
    When user adds headers as map to the request
#    QueryParams
    When user adds Query Parameter named "Parameter Key" with value "Parameter value" to the request
    When user adds Query Params to the request
      | Parameter Key | Parameter Value |
      | Key1          | Val1            |
    When user adds Query Param as map to the request
#    FormParams
    When user adds Form Parameter named "Parameter Key" with value "Parameter value" to the request
    When user adds form parameters
      | Parameter Key | Parameter Key |
      | key           | val           |
    When user adds Form Param as map to the request
#    PathParams
    When user adds Path Parameter named "Parameter Key" with value "Parameter value" to the request
    When user adds path parameters
      | Parameter Key | Parameter Key |
      | key           | val           |
    When user adds Path Param as map to the request
#    multipart files
    When user sets Content-Type as "multipart/form-data"
    When user adds multipart file "SampleMultipartFile.txt" with "file" control name
    When user adds multipart file "SampleMultipartFile.txt" with "file" control name and "text/html" MIME type
#   AddBody
    When user adds "POSTReqBody" body
#    Hit Request
    When user makes a "post" request
#    Extract Keys
    When user extracts "data.email" from response
    When user extracts the following keys from response
      | Key Path   |
      | data.email |
#    Add Extracted key to request body
    When user adds extracted value of key "data.email" at "user.email" path in "POSTReqBody" request body

    #---------------------------------------------- multiple requests------------------------------------

    Given user creates a new request named "requestTwo" and sets "custom" as endpoint
    When user sets Content-Type as "application/json" for "requestTwo" request
    When user sets relaxed HTTPS validation for "requestTwo" request
    When user sets url encoding to "false" for "requestTwo" request
# Headers
    When user adds header named "header1" with value "value1" for "requestTwo" request
    When user adds headers to "requestTwo" request
      | Header Name | Header Value |
      | header1     | value1       |
    When user adds headers as map for "requestTwo" request
#  QueryParams
    When user adds Query Parameter named "Parameter Key" with value "Parameter value" to "requestTwo" request
    When user adds Query Params to "requestTwo" request
      | Parameter Key | Parameter Value |
      | Key1          | Val1            |
    When user adds Query Param as map for "requestTwo" request
#    PathParams
    When user adds Path Parameter named "Parameter Key" with value "Parameter value" to "requestTwo" request
    When user adds path parameters to "PostData" request
      | Parameter Key | Parameter Key |
      | key           | val           |
    When user adds Path Param as map for "requestTwo" request
#    FormParams
    When user adds Form Parameter named "Parameter Key" with value "Parameter value" to "requestTwo" request
    When user adds form parameters to "PostData" request
      | Parameter Key | Parameter Key |
      | key           | val           |
    When user adds Form Param as map for "requestTwo" request
#    Make request
    When user makes "get" request for "requestTwo" request

    Given user creates a new request named "requestThree" and sets "custom" as endpoint
#    Multipart Files
    When user sets Content-Type as "multipart/json" for "requestThreer" request
    When user adds multipart file "SampleMultipartFile.txt" with "file" control name to "requestThree" request
    When user adds multipart file "SampleMultipartFile.txt" with "file" control name and "text/html" MIME type to "requestThree" request
#    Add Body
    When user adds "requestbodysample" body to "requestThree" request
#    Extract Keys
    When user extracts the following keys from "getSingleUser" response
      | Key Path   |
      | data.email |
    When user extracts "data.email" from "requestThree" response
#    Add extracted key to request body
    When user adds extracted value of key "data.email" at "email" path in "requestBodySample" request body of "requestThree" request


  @Validations
    #single request
  Scenario: Response Validation functions
    Then user verifies state of key-value in response body
      | Key             | operation    | Value     |
      | type            | equals       | donut     |
      | type            | not equals   | Sprinkled |
      | topping[1].type | contains     | Glazed    |
      | topping[1].type | not contains | janeta    |
    Then user verifies the response matches "testSchema" schema
    Then user verifies the size of array with key "topping" is "7" in response body
    Then user verifies the response time taken is less than "7" seconds in response body
    Then user verifies the presence of message "donut" in response body
    Then user verifies value "600" is found in Json array response for "prices" key in response body
    Then user verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices" in response
    Then user verifies "200" status code

    #multiple request
    Then user verifies state of key-value in response body for "requestTwo" response
      | Key             | operation    | Value   |
      | type            | equals       | donut   |
      | type            | not equals   | Glaazed |
      | topping[1].type | contains     | Glazed  |
      | topping[1].type | not contains | janeta  |
    Then user verifies the response matches "testSchema" schema for "requestTwo" response
    Then user verifies the size of array with key "topping" is "7" for "requestTwo" response body
    Then user verifies the size of array with key "id" is "0" for "requestTwo" response body
    Then user verifies the response time taken is less than "7" seconds for "requestTwo" response
    Then user verifies the presence of message "donut" in response body for "requestTwo" response body
    Then user verifies response code as "200" for "requestTwo" response
    Then user verifies value "600" is found in Json array response for "prices" key for "requestTwo" response
    Then user verifies value "600" is found in Json array response for key with Json Path "custom.value1.value2.prices" for "requestTwo" response

  @Clear
  Scenario: Memory clearing functions
    When user deletes "DeleteData" request
    When user deletes "DeleteData" response
    When user clears all requests
    When user clears all responses


    #Json Comparison
    When user compares response "response1" with "response2" response using JSON Comparator