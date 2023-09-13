@Demo
Feature: Sample Detailed Scenarios

  @DetailedScenario1
  Scenario: Create User from other user's data in Detail
    Given user creates a new request named "GetUserDetails" request and sets "read" as endpoint
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

    Given user creates a new request named "CreateUser" request and sets "create" as endpoint
    When user adds headers to "CreateUser" request
      | Header Name | Header Value |
      | Accept      | */*          |
    When user adds extracted value of key "data.email" at "user[0].email" path in "POSTReqBody" request body
    When user sends "POST" request for "CreateUser" request
    Then user verifies "201" status code


  @PostPut
  Scenario: Create and update a user in Detail
    Given user creates a new request named "CreateUser" request and sets "create" as endpoint
    When user adds headers to "CreateUser" request
      | Header Name | Header Value |
      | Accept      | */*          |
    When user adds extracted value of key "data.email" at "email" path in "POSTReqBody" request body
    When user adds "POSTReqBody" body to "CreateUser" request
    When user sends "POST" request for "CreateUser" request
    Then user verifies "201" status code

    Given user creates a new request named "UpdateUser" request and sets "update" as endpoint
    When user adds "PUTReqBody" body to "UpdateUser" request
    When user sends "PUT" request for "UpdateUser" request
    Then user verifies response code as "200" for "UpdateUser" response
