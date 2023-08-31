@Demo
Feature: Sample Detailed Scenarios

  @DetailedScenario1
  Scenario: Detailed Scenario 1
    Given user creates a new request named "getSingleUser" and sets "read" as endpoint
    When user makes a "GET" request
    Then user verifies "200" status code
    Then user verifies state of key-value in response body
      | Key             | operation    | Value                  |
      | data.first_name | equals       | Janet                  |
      | data.first_name | not equals   | Janeta                 |
      | data.email      | contains     | janet.weaver@reqres.in |
      | data.email      | not contains | janeta                 |
    And user extracts the following keys from response
      | Key Path   |
      | data.email |

    Given user creates a new request named "PostData" and sets "create" as endpoint
    When user adds headers to "PostData" request
      | Header Name | Header Value |
      | Accept      | */*          |
    And user adds "POSTReqBody" body to "PostData" request
    And user makes "POST" request for "PostData" request
    Then user verifies "201" status code


  @DetailedScenario2
  Scenario: Detailed Scenario 2
    Given user creates a new request named "PostNewData" and sets "create" as endpoint
    When user adds headers to "PostNewData" request
      | Header Name | Header Value |
      | Accept      | */*          |
    And user adds "POSTReqBody" body to "PostNewData" request
    When user makes "POST" request for "PostNewData" request
    Then user verifies "201" status code
    Given user creates a new request named "PutData" and sets "update" as endpoint
    When user adds "PUTReqBody" body to "PutData" request
    And user makes "PUT" request for "PutData" request
    Then user verifies response code as "200" for "PutData" response

