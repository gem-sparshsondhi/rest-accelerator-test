package com.gemini.EJ.tests.stepdefinitions;

import com.fasterxml.jackson.databind.JsonMappingException;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.pages.PageObject;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UtilityFunctions extends PageObject {
    public static Logger logger = LoggerFactory.getLogger(UtilityFunctions.class);
    public static List<RequestSpecification> reqSpecList = new ArrayList<>();
    public static List<Response> responseList = new ArrayList<>();
    public static int activeIndex = -1;

    //Request building functions


    /**
     * Creates a new RequestSpecification for the user to modify and use as and when required
     * <p>This is the starting point for all new requests and must be called for successful Rest request.
     * <p>
     * The index at which the latest RequestSpecification is stored is also noted.
     */
    public static void createNewRequest() {
        RequestSpecification reqSpec = SerenityRest.given();
        reqSpecList.add(reqSpec);
        activeIndex++;
    }

    /**
     * Sets the BaseURI of the latest RequestSpecification
     *
     * @param URI
     */
    public static void setBaseURI(String URI) {
        reqSpecList.get(activeIndex).baseUri(URI);
    }

    /**
     * Sets the BasePath of the latest RequestSpecification
     *
     * @param path
     */
    public static void setBasePath(String path) {
        reqSpecList.get(activeIndex).basePath(path);
    }

    /**
     * Sets a Header to the latest RequestSpecification
     *
     * @param headerName
     * @param headerValue
     */
    public static void addHeader(String headerName, String headerValue) {
        reqSpecList.get(activeIndex).header(headerName, headerValue);
    }

    /**
     * Sets the Headers of the latest RequestSpecification
     *
     * @param headers
     */
    public static void addHeaders(HashMap<String, String> headers) {
        reqSpecList.get(activeIndex).headers(headers);
    }

    /**
     * Sets a Query Parameter to the latest RequestSpecification
     *
     * @param paramName
     * @param paramValue
     */
    public static void addQueryParam(String paramName, String paramValue) {
        reqSpecList.get(activeIndex).queryParam(paramName, paramValue);
    }

    /**
     * Sets the Query Parameters of the latest RequestSpecification
     *
     * @param params
     */
    public static void addQueryParams(HashMap<String, String> params) {
        reqSpecList.get(activeIndex).queryParams(params);
    }

    /**
     * Sets the ContentType of the latest RequestSpecification
     *
     * @param contentType
     */
    public static void setContentType(String contentType) {
        reqSpecList.get(activeIndex).contentType(contentType);
    }

    /**
     * Configures the latest RequestSpecification to use relaxed HTTP validation
     */
    public static void setRelaxedHttpsValidation() {
        reqSpecList.get(activeIndex).relaxedHTTPSValidation();
    }

    /**
     * Toggles URL Encoding for the latest RequestSpecification
     *
     * @param encodingStatus
     */
    public static void setUrlEncoding(boolean encodingStatus) {
        reqSpecList.get(activeIndex).urlEncodingEnabled(encodingStatus);
    }

    /**
     * Sets the Body of the latest RequestSpecification from a specified file.
     * The specified file must be present in the requestbodies directory of the resources package
     *
     * @param fileName
     */
    public static void userSetsRequestBodyFromFile(String fileName) {
        if (fileName.contains(".json"))
            fileName = fileName.split("\\.json")[0];         // remove the extension in case it's present
        InputStream inputStream = UtilityFunctions.class.getResourceAsStream("/requestbodies/" + fileName + ".json");
        String content;
        if (inputStream == null) {
            throw new RuntimeException("Could not find resource file");
        }
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            content = scanner.useDelimiter("\\A").next();
        }
        if (content == null) {
            throw new RuntimeException("Could not read resource file");
        }
        reqSpecList.get(activeIndex).body(content);
    }

    /**
     * Adds a file to be uploaded. Also sets the contentType of the active request to "multipart/form-data" automatically
     * <p>
     * MIME type is auto-set
     * @param controlName
     * @param filename
     */
    public static void addMultipartFileToRequest(String controlName, String filename) {
        if (filename.split("\\.").length == 1)
            Assert.fail("No File extension detected. Please provide filename with extension");
        File file = new File("src/test/resources/multipartfiles/" + filename);
        reqSpecList.get(activeIndex).multiPart(controlName, file);
    }

    /**
     * Adds a file to be uploaded. Also sets the contentType of the active request to "multipart/form-data" automatically
     * <p>Sets the MIME type according to the parameter passed
     * @param controlName
     * @param filename
     * @param mimeType
     */
    public static void addMultipartFileToRequest(String controlName, String filename, String mimeType) {
        if (filename.split("\\.").length == 1)
            Assert.fail("No File extension detected. Please provide filename with extension");
        File file = new File("src/test/resources/multipartfiles/" + filename);
        reqSpecList.get(activeIndex).multiPart(controlName, file,mimeType);
    }

    /**
     * Initiates a request based on the latest RequestSpecification.<p>
     * The Response is then saved into another List, with the same index as the RequestSpecification that created it.
     *
     * @param requestType
     */
    public static void makeRequest(String requestType) {
        Response response = null;
        switch (requestType.toLowerCase()) {
            case "get":
                response = reqSpecList.get(activeIndex)
                        .log().all()
                        .when().get();
                break;
            case "post":
                response = reqSpecList.get(activeIndex)
                        .log().all()
                        .when().post();
                break;
            case "put":
                response = reqSpecList.get(activeIndex)
                        .log().all()
                        .when().put();
                break;
            case "patch":
                response = reqSpecList.get(activeIndex)
                        .log().all()
                        .when().patch();
                break;
            case "delete":
                response = reqSpecList.get(activeIndex)
                        .log().all()
                        .when().delete();
                break;
            default:
                logger.info("No such request type");
                Assert.fail("No such request type");

        }
        logger.info("\n\nResponse Body:\n" + response.getBody().asPrettyString());
        responseList.add(response);
    }

    //Validation Functions

    /**
     * Verifies whether a particular key-value pair is present in the Latest response<p>
     * Uses JsonPath to determine the value.
     *
     * @param pathProvided
     * @param expectedValue
     */

    public static void verifyKeyAndValuePresence(String pathProvided, String expectedValue) {
        try {
            Response res = responseList.get(activeIndex);
            Object valueFoundAtPath = res.jsonPath().getString(pathProvided);
            if (!StringUtils.equals(expectedValue, String.valueOf(valueFoundAtPath))) {
                if (pathProvided.split("\\.").length == 1) {
                    logger.info("Couldn't find the specified value for Key: " + pathProvided);
                    Assert.fail("Couldn't find the specified value for Key: " + pathProvided);
                } else {
                    logger.info("Couldn't find the specified value for path: " + pathProvided);
                    Assert.fail("Couldn't find the specified value for path: " + pathProvided);
                }
            }
            if (pathProvided.split("\\.").length == 1) {
                logger.info("Successfully verified presence of " + expectedValue + " in response for provided Key: \"" + pathProvided + "\"");
            } else
                logger.info("Successfully verified presence of " + expectedValue + " in response for provided path: \"" + pathProvided + "\"");
        } catch (IllegalArgumentException e) {
            if (pathProvided.split("\\.").length == 1) {
                logger.info("No such key found in Response. Kindly recheck the path: " + pathProvided + ". If the key is nested, Kindly provide the JsonPath to the Key");
                Assert.fail("No such key found in Response. Kindly recheck the path: " + pathProvided + ". If the key is nested, Kindly provide the JsonPath to the Key");
            } else {
                logger.info("No such path found in Response. Kindly recheck the path: " + pathProvided);
                Assert.fail("No such path found in Response. Kindly recheck the path: " + pathProvided);
            }
        }

    }

    /**
     * Verifies whether a particular key-value pair is present in a specified response<p>
     * Uses JsonPath to determine the value.
     *
     * @param pathProvided
     * @param expectedValue
     * @param responseNo
     */
    public static void verifyKeyAndValuePresence(String pathProvided, String expectedValue, String responseNo) {
        int idx = -1;
        try {
            idx = Integer.parseInt(responseNo) - 1;
        } catch (NumberFormatException e) {
            logger.info("Invalid Response Number format. Please recheck the provided Response Number: " + responseNo);
            Assert.fail("Invalid Response Number format. Please recheck the provided Response Number: " + responseNo);
        }
        if (idx > activeIndex) {
            logger.info("No such response number found. Please recheck the provided Response Number: " + responseNo);
            Assert.fail("No such response number found. Please recheck the provided Response Number: " + responseNo);
        } else {
            boolean keyProvided = pathProvided.split("\\.").length == 1;
            try {
                Response res = responseList.get(idx);
                Object valueFoundAtPath = res.jsonPath().getString(pathProvided);
                if (!StringUtils.equals(expectedValue, String.valueOf(valueFoundAtPath)))
                    if (keyProvided) {
                        logger.info("Couldn't find the specified value for Key: " + pathProvided);
                        Assert.fail("Couldn't find the specified value for Key: " + pathProvided);
                    } else {
                        logger.info("Couldn't find the specified value for path: " + pathProvided);
                        Assert.fail("Couldn't find the specified value for path: " + pathProvided);
                    }
                if (keyProvided)
                    logger.info("Successfully verified presence of " + expectedValue + " in response for provided Key: \"" + pathProvided + "\"");
                else
                    logger.info("Successfully verified presence of " + expectedValue + " in response for provided path: \"" + pathProvided + "\"");
            } catch (IllegalArgumentException e) {
                if (keyProvided) {
                    logger.info("No such key found in Response. Kindly recheck the path: " + pathProvided + ". If the key is nested, Kindly provide the JsonPath to the Key");
                    Assert.fail("No such key found in Response. Kindly recheck the path: " + pathProvided + ". If the key is nested, Kindly provide the JsonPath to the Key");
                } else {
                    logger.info("No such path found in Response. Kindly recheck the path: " + pathProvided);
                    Assert.fail("No such path found in Response. Kindly recheck the path: " + pathProvided);
                }
            }

        }
    }

    /**
     * Verifies whether the latest response matches the specified JSON Schema
     *
     * @param schemaName
     */
    public static void verifyResponseSchema(String schemaName) {
        Response res = responseList.get(activeIndex);
        try {
            res.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\schemas\\" + schemaName + ".json")));
        } catch (Exception e) {
            if (e.getMessage().contains("JsonMappingException") || e.getMessage().contains("IOException")) {
                logger.info("Failed to validate Schema. Body may not match expectations. Exception: " + e.getMessage());
                Assert.fail("Failed to validate Schema. Body may not match expectations. Exception: " + e.getMessage());
            } else if (e.getMessage().contains("FileNotFoundException")) {
                logger.info("Failed to validate Schema. Could not find the reference Schema");
                Assert.fail("Failed to validate Schema. Could not find the reference Schema");
            } else if (e.getMessage().contains("JsonParseException")) {
                logger.info("Failed to validate Schema. Reference Schema is invalid");
                Assert.fail("Failed to validate Schema. Reference Schema is invalid");
            } else {
                logger.info("Failed to validate Schema due to Exception: " + e);
                Assert.fail("Failed to validate Schema due to Exception: " + e);
            }
        }
        logger.info("Response Schema verified successfully.");
    }

    /**
     * Verifies whether the specified response matches the specified JSON Schema
     *
     * @param schemaName
     * @param responseNo
     */
    public static void verifyResponseSchema(String schemaName, String responseNo) {
        int idx = -1;
        try {
            idx = Integer.parseInt(responseNo) - 1;
        } catch (NumberFormatException e) {
            Assert.fail("Invalid Response Number format. Please recheck the provided Response Number: " + responseNo);
        }
        if (idx > activeIndex) {
            logger.info("No such response number found. Please recheck the provided Response Number: " + responseNo);
            Assert.fail("No such response number found. Please recheck the provided Response Number: " + responseNo);
        } else {
            Response res = responseList.get(idx);
            try {
                res.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\schemas\\" + schemaName + ".json")));
            } catch (Exception e) {
                if (e instanceof JsonMappingException || e instanceof FileNotFoundException || e instanceof IOException) {
                    logger.info("Failed to validate Schema. Body may not match expectations. Exception: " + e);
                    Assert.fail("Failed to validate Schema. Body may not match expectations. Exception: " + e);
                } else {
                    logger.info("Failed to validate Schema due to Exception: " + e);
                    Assert.fail("Failed to validate Schema due to Exception: " + e);
                }
            }
        }
        logger.info("Response Schema verified successfully.");
    }

    /**
     * Verifies whether the length of the specified JSONArray (Using JsonPath) matches expectations in the latest response
     * <p>
     * Uses JsonPath to search for the value and verifies if it is a JSONArray or not beforehand.
     *
     * @param pathProvided
     * @param size
     */
    public static void verifyJsonArraySize(String pathProvided, int size) {
        Response res = responseList.get(activeIndex);
        int arrSize = -1;
        Object valueFoundAtPath = res.jsonPath().get(pathProvided);
        if (ObjectUtils.allNull(valueFoundAtPath)) {
            if (size == 0) {
                logger.info("No array found for specified key: " + pathProvided + ". Size 0 verified.");
                return;
            } else {
                logger.info("No value found for for provided key: \"" + pathProvided + "\"");
                Assert.fail("No value found for for provided key: \"" + pathProvided + "\"");
            }
        } else if (valueFoundAtPath instanceof ArrayList) {
            ArrayList arr = (ArrayList) valueFoundAtPath;
            arrSize = arr.size();
        } else {
            logger.info("Value of provided key: " + pathProvided + " is not a JSONArray. Can't validate the size");
            Assert.fail("Value of provided key: " + pathProvided + " is not a JSONArray. Can't validate the size");
        }

        if (arrSize == size) {
            logger.info("The array with size " + size + " exists");
        } else {
            logger.info("The array with size " + size + " does not exist");
            Assert.fail("The array with size " + size + " does not exist");
        }
    }

    /**
     * Verifies whether the length of the specified JSONArray (Using JsonPath) matches expectations in the specified response
     * <p>
     * Uses JsonPath to search for the value and verifies if it is a JSONArray or not beforehand.
     *
     * @param pathProvided
     * @param size
     */
    public static void verifyJsonArraySize(String pathProvided, int size, String responseNo) {
        int idx = -1;
        try {
            idx = Integer.parseInt(responseNo) - 1;
        } catch (NumberFormatException e) {
            Assert.fail("Invalid Response Number format. Please recheck the provided Response Number: " + responseNo);
        }
        if (idx > activeIndex) {
            logger.info("No such response number found. Please recheck the provided Response Number: " + responseNo);
            Assert.fail("No such response number found. Please recheck the provided Response Number: " + responseNo);
        } else {
            Response res = responseList.get(idx);
            int arrSize = -1;
            Object valueFoundAtPath = res.jsonPath().get(pathProvided);
            if (ObjectUtils.allNull(valueFoundAtPath)) {
                if (size == 0) {
                    logger.info("No array found for specified key: " + pathProvided + ". Size 0 verified.");
                    return;
                } else {
                    logger.info("No value found for for provided key: \"" + pathProvided + "\"");
                    Assert.fail("No value found for for provided key: \"" + pathProvided + "\"");
                }
            } else if (valueFoundAtPath instanceof ArrayList) {
                ArrayList arr = (ArrayList) valueFoundAtPath;
                arrSize = arr.size();
            } else {
                logger.info("Value of provided key: " + pathProvided + " is not a JSONArray. Can't validate the size");
                Assert.fail("Value of provided key: " + pathProvided + " is not a JSONArray. Can't validate the size");
            }

            if (arrSize == size) {
                logger.info("The array with size " + size + " exists");
            } else {
                logger.info("The array with size " + size + " does not exist");
                Assert.fail("The array with size " + size + " does not exist");
            }
        }

    }

    /**
     * Verifies if the latest response was returned within the specified time limit or not.
     * <p>
     * Trims the time limit down to 5 seconds as an API is not expected to take longer under normal circumstances.
     *
     * @param expectedTimeInSeconds
     */
    public static void verifyResponseTime(float expectedTimeInSeconds) {
        Response res = responseList.get(activeIndex);
        if (expectedTimeInSeconds > 5) {
            expectedTimeInSeconds = 5;
            logger.info("Expected time is much more. Trimming it down to 5 seconds");
        }
        long actualTime = res.getTime();
        logger.info("Actual Time: " + actualTime + " milliseconds and expected time is: " + expectedTimeInSeconds * 1000 + " milliseconds");
        Assert.assertTrue("Response took longer than expected. Expected Time: Atleast " + expectedTimeInSeconds * 1000 + " milliseconds. Actual time: " + actualTime + " milliseconds", actualTime < expectedTimeInSeconds * 1000);

    }

    /**
     * Verifies if the specified response was returned within the specified time limit or not.
     * <p>
     * Trims the time limit down to 5 seconds as an API is not expected to take longer under normal circumstances.
     *
     * @param expectedTimeInSeconds
     */
    public static void verifyResponseTime(float expectedTimeInSeconds, String responseNo) {
        int idx = -1;
        try {
            idx = Integer.parseInt(responseNo) - 1;
        } catch (NumberFormatException e) {
            Assert.fail("Invalid Response Number format. Please recheck the provided Response Number: " + responseNo);
        }
        if (idx > activeIndex) {
            logger.info("No such response number found. Please recheck the provided Response Number: " + responseNo);
            Assert.fail("No such response number found. Please recheck the provided Response Number: " + responseNo);
        } else {
            Response res = responseList.get(idx);
            if (expectedTimeInSeconds > 5) {
                expectedTimeInSeconds = 5;
                logger.info("Expected time is much more. Trimming it down to 5 seconds");
            }
            long actualTime = res.getTime();
            logger.info("Actual Time: " + actualTime + " milliseconds and expected time is: " + expectedTimeInSeconds * 1000 + " milliseconds");
            Assert.assertTrue("Response took longer than expected. Expected Time: Atleast " + expectedTimeInSeconds * 1000 + " milliseconds. Actual time: " + actualTime + " milliseconds", actualTime < expectedTimeInSeconds * 1000);
        }
    }

    /**
     * Verifies if the latest response contains a particular string in the response body
     *
     * @param expectedMessage
     * @param responses
     */
    public static void messageValidation(String expectedMessage, Response... responses) {
        Response res = responseList.get(activeIndex);
        String responseMessage = res.body().asPrettyString();
        if (responseMessage.contains(expectedMessage)) {
            logger.info("Successfully found " + expectedMessage + " is Response body", true);
        } else {
            logger.info("Unable to find \"" + expectedMessage + "\" in Response body");
            Assert.fail("Unable to find \"" + expectedMessage + "\" in Response body");
        }
    }

    /**
     * Verifies if the specified response contains a particular string in the response body
     *
     * @param expectedMessage
     * @param responseNo
     */
    public static void messageValidation(String expectedMessage, String responseNo) {
        int idx = -1;
        try {
            idx = Integer.parseInt(responseNo) - 1;
        } catch (NumberFormatException e) {
            Assert.fail("Invalid Response Number format. Please recheck the provided Response Number: " + responseNo);
        }
        if (idx > activeIndex) {
            logger.info("No such response number found. Please recheck the provided Response Number: " + responseNo);
            Assert.fail("No such response number found. Please recheck the provided Response Number: " + responseNo);
        } else {
            Response res = responseList.get(idx);
            String responseMessage = res.body().asPrettyString();
            if (responseMessage.contains(expectedMessage)) {
                logger.info("Successfully found " + expectedMessage + " is Response body", true);
            } else {
                logger.info("Unable to find \"" + expectedMessage + "\" in Response body");
                Assert.fail("Unable to find \"" + expectedMessage + "\" in Response body");
            }
        }
    }

    /**
     * Verifies if the status code of the latest response matches expectations
     *
     * @param statusCode
     */
    public static void verifyStatusCode(int statusCode) {
        Response res = responseList.get(activeIndex);
        Assert.assertEquals("Status code didn't match", statusCode, res.getStatusCode());
        logger.info("Status code verified");

    }

    /**
     * Verifies if the status code of the specified response matches expectations
     *
     * @param statusCode
     * @param responseNo
     */
    public static void verifyStatusCode(int statusCode, String responseNo) {
        int idx = -1;
        try {
            idx = Integer.parseInt(responseNo) - 1;
        } catch (NumberFormatException e) {
            Assert.fail("Invalid Response Number format. Please recheck the provided Response Number: " + responseNo);
        }
        if (idx > activeIndex) {
            logger.info("No such response number found. Please recheck the provided Response Number: " + responseNo);
            Assert.fail("No such response number found. Please recheck the provided Response Number: " + responseNo);
        } else {
            Response res = responseList.get(idx);
            Assert.assertEquals("Status code didn't match", statusCode, res.getStatusCode());
            logger.info("Status code verified");
        }
    }

    /**
     * Verifies presence of a particular element in a JSONArray in the latest response
     * <p>
     * Uses JsonPath to search for the value and verifies if it is a JSONArray or not beforehand.
     * <p>
     * Does not work with Array of JSONObjects as they will have an extended JsonPath.
     *
     * @param pathProvided
     * @param expectedValue
     */
    public static void verifyValueInJsonArray(String pathProvided, String expectedValue, Response... responses) {
        Response res = responseList.get(activeIndex);
        Object valueFoundAtPath = res.jsonPath().get(pathProvided);
        if (ObjectUtils.allNull(valueFoundAtPath)) {
            logger.info("No value found for for provided key: \"" + pathProvided + "\"");
            Assert.fail("No value found for for provided key: \"" + pathProvided + "\"");
        } else if (valueFoundAtPath instanceof ArrayList) {
            ArrayList arr = (ArrayList) valueFoundAtPath;
            if (!(arr.get(0) instanceof JSONObject)) {
                boolean found = false;
                for (Object obj : arr) {
                    found = String.valueOf(obj).equals(String.valueOf(expectedValue));
                    if (found) break;
                }
                if (!found) {
                    logger.info("Couldn't find " + expectedValue + " in response for provided key: \"" + pathProvided + "\"");
                    Assert.fail("Couldn't find " + expectedValue + " in response for provided key: \"" + pathProvided + "\"");
                }
            } else {
                logger.info("Value for provided key: " + pathProvided + " is a JSONArray of JSONObjects. Can't validate the value");
                Assert.fail("Value for provided key: " + pathProvided + " is a JSONArray of JSONObjects. Can't validate the value");
            }
        } else {
            logger.info("Value for provided key: " + pathProvided + " is not a JSONArray. Can't validate the value");
            Assert.fail("Value for provided key: " + pathProvided + " is not a JSONArray. Can't validate the value");
        }
        if (pathProvided.split("\\.").length == 1)
            logger.info("Successfully verified presence of " + expectedValue + " in response for provided key: \"" + pathProvided + "\"");
        else
            logger.info("Successfully verified presence of " + expectedValue + " in response for provided path: \"" + pathProvided + "\"");

    }

    /**
     * Verifies presence of a particular element in a JSONArray in the specified response
     * <p>
     * Uses JsonPath to search for the value and verifies if it is a JSONArray or not beforehand.
     * <p>
     * Does not work with Array of JSONObjects as they will have an extended JsonPath.
     *
     * @param pathProvided
     * @param expectedValue
     * @param responseNo
     */
    public static void verifyValueInJsonArray(String pathProvided, String expectedValue, String responseNo) {
        int idx = -1;
        try {
            idx = Integer.parseInt(responseNo) - 1;
        } catch (NumberFormatException e) {
            Assert.fail("Invalid Response Number format. Please recheck the provided Response Number: " + responseNo);
        }
        if (idx > activeIndex) {
            logger.info("No such response number found. Please recheck the provided Response Number: " + responseNo);
            Assert.fail("No such response number found. Please recheck the provided Response Number: " + responseNo);
        } else {
            Response res = responseList.get(idx);
            Object valueFoundAtPath = res.jsonPath().get(pathProvided);
            if (ObjectUtils.allNull(valueFoundAtPath)) {
                logger.info("No value found for for provided key: \"" + pathProvided + "\"");
                Assert.fail("No value found for for provided key: \"" + pathProvided + "\"");
            } else if (valueFoundAtPath instanceof ArrayList) {
                ArrayList arr = (ArrayList) valueFoundAtPath;
                if (!(arr.get(0) instanceof JSONObject)) {
                    boolean found = false;
                    for (Object obj : arr) {
                        found = String.valueOf(obj).equals(String.valueOf(expectedValue));
                        if (found) break;
                    }
                    if (!found) {
                        logger.info("Couldn't find " + expectedValue + " in response for provided key: \"" + pathProvided + "\"");
                        Assert.fail("Couldn't find " + expectedValue + " in response for provided key: \"" + pathProvided + "\"");
                    }
                } else {
                    logger.info("Value for provided key: " + pathProvided + " is a JSONArray of JSONObjects. Can't validate the value");
                    Assert.fail("Value for provided key: " + pathProvided + " is a JSONArray of JSONObjects. Can't validate the value");
                }
            } else {
                logger.info("Value for provided key: " + pathProvided + " is not a JSONArray. Can't validate the value");
                Assert.fail("Value for provided key: " + pathProvided + " is not a JSONArray. Can't validate the value");
            }
            if (pathProvided.split("\\.").length == 1)
                logger.info("Successfully verified presence of " + expectedValue + " in response for provided key: \"" + pathProvided + "\"");
            else
                logger.info("Successfully verified presence of " + expectedValue + " in response for provided path: \"" + pathProvided + "\"");
        }
    }
}
