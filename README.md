# rest-accelerator-test
Repo for Rest-Accelerator functions

The functions in this repo can be used as a Starting point for all rest-assured automation suites

Compiled with: JDK 11

Framework used: Serenity

Build Tool: Gradle 8.0.2

Necessary Dependencies: 
* io.rest-assured:json-schema-validator:5.3.0 (or higher)

Files not directly linked to code:
* src/test/resources/fake-server-API/DB_data.json
* src/test/resources/fake-server-API/ReceivedResponse.json

The abovw 2 files outline the data present at http://localhost:3000 and http://localhost:3000/items respectively. This data has been used to test the Utility functions, and can be utilized for further testing with the setup of Fake-Server from the typicode repository

Typicode Repository: https://github.com/typicode/json-server.git
