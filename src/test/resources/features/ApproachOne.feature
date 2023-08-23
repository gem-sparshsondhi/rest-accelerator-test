
Feature: Test

  @Get
  Scenario:GET Request
    Given user creates a new request named "getSingleUser" request and sets "read" as endpoint
    When user makes a "GET" request
    Then user verifies state of key-value in response body
      | Key             | operation    | Value                   |
      | data.first_name | equals       | Janet                   |
      | data.first_name | not equals   | Janeta                  |
      | data.email      | contains     | janet.weaver@reqres.in  |
      | data.email      | not contains | janeta.weaver@reqres.in |
    When user extracts value from "data.email" response

  @PostPutPatch @Samples
  Scenario:Multiple Requests
    Given user creates a new request named "PostData" request and sets "create" as endpoint
    When user adds headers to "PostData" request
      | Header Name | Header Value |
      | Accept      | */*          |
    When user adds "POSTReqBody" body to "PostData" request
    When user adds extracted value of key "data.email" in "POSTReqBody" request body
    When user makes "POST" request for "PostData" request
    Then user verifies "201" status code

    Given user creates a new request named "PutData" request and sets "update" as endpoint
    When user adds "PUTReqBody" body to "PutData" request
    When user makes "PUT" request for "PutData" request
    Then user verifies response code as "200" for "PutData" response


#    Given user creates a new request named "PatchData" request and sets "patch" as endpoint
#    When user adds headers to "PatchData" request
#      | Header Name  | Header Value |
#      | Accept       | */*          |
#      | Content-Type | text/plain   |
#    When user adds "PATCHReqBody" body to "PatchData" request
#    When user makes "PATCH" request for "PatchData" request
#    Then user verifies "200" status code

  @Delete
  Scenario:DELETE Request
    Given user creates a new request named "DeleteData" request and sets "delete" as endpoint
    When user makes a "DELETE" request
    Then user verifies "204" status code

