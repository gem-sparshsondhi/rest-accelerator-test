@Demo1
Feature: Test

  @Get
  Scenario:GET Request
    Given user creates a new request named "getSingleUser" request and sets "read" as endpoint
    When user sends a "GET" request
    Then user verifies "200" status code
    Then user verifies state of key-value in response body
      | Key             | operation    | Value                  |
      | data.first_name | equals       | Janet                  |
      | data.first_name | not equals   | Janeta                 |
      | data.email      | contains     | janet.weaver@reqres.in |
      | data.email      | not contains | janeta                 |
    When user extracts the following keys from response
      | Key Path   |
      | data.email |
    Given user creates a new request named "PostData" request and sets "create" as endpoint
    When user adds headers to "PostData" request
      | Header Name | Header Value |
      | Accept      | */*          |
    When user adds extracted value of key "data.email" at "data[0].email" path in "POSTReqBody" request body
    When user sends "POST" request for "PostData" request
    Then user verifies "201" status code


#  @PostPutPatch @Samples @Get
#  Scenario:Multiple Requests
#    Given user creates a new request named "PostData" request and sets "create" as endpoint
#    When user adds headers to "PostData" request
#      | Header Name | Header Value |
#      | Accept      | */*          |
#    When user adds extracted value of key "data.email" at "email" path in "POSTReqBody" request body
#    When user adds "POSTReqBody" body to "PostData" request
#    When user sends "POST" request for "PostData" request
#    Then user verifies "201" status code
#
#    Given user creates a new request named "PutData" request and sets "update" as endpoint
#    When user adds "PUTReqBody" body to "PutData" request
#    When user sends "PUT" request for "PutData" request
#    Then user verifies response code as "200" for "PutData" response
#
#    Given user creates a new request named "PatchData" request and sets "patch" as endpoint
#    When user adds headers to "PatchData" request
#      | Header Name  | Header Value |
#      | Accept       | */*          |
#      | Content-Type | text/plain   |
#    When user adds "PATCHReqBody" body to "PatchData" request
#    When user sends "PATCH" request for "PatchData" request
#    Then user verifies "200" status code
#
#  @Delete
#  Scenario:DELETE Request
#    Given user creates a new request named "DeleteData" request and sets "delete" as endpoint
#    When user sends a "DELETE" request
#    Then user verifies "204" status code
#
