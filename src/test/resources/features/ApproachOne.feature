@Demo
Feature: Sample Detailed Scenarios

  @DetailedScenario1
  Scenario: Create Client Account from other client's data (Detailed)
    Given user creates a new request named "GetClientDetails" request and sets "read" as endpoint
    When user sends a "GET" request
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

    Given user creates a new request named "CreateClientAccount" request and sets "create" as endpoint
    When user adds headers to "CreateClientAccount" request
      | Header Name | Header Value |
      | Accept      | */*          |
    When user adds extracted value of key "data.email" at "user[0].email" path in "POSTReqBody" request body
    When user sends "POST" request for "CreateClientAccount" request
    Then user verifies "201" status code


  @DetailedScenario2
  Scenario: Create and update a Client Account (Detailed)
    Given user creates a new request named "CreateClientAccount" request and sets "create" as endpoint
    When user adds headers to "CreateClientAccount" request
      | Header Name | Header Value |
      | Accept      | */*          |
    When user adds extracted value of key "data.email" at "email" path in "POSTReqBody" request body
    When user adds "POSTReqBody" body to "CreateClientAccount" request
    When user sends "POST" request for "CreateClientAccount" request
    Then user verifies "201" status code

    Given user creates a new request named "UpdateClientAccount" request and sets "update" as endpoint
    When user adds "PUTReqBody" body to "UpdateClientAccount" request
    When user sends "PUT" request for "UpdateClientAccount" request
    Then user verifies response code as "200" for "UpdateClientAccount" response
