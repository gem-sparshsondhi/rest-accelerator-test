package com.gemini.EJ.tests.stepdefinitions;

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
import java.util.ArrayList;
import java.util.List;

public class UtilityFunctions extends PageObject {
    public static Logger logger = LoggerFactory.getLogger(UtilityFunctions.class);
    public static List<RequestSpecification> reqSpecList = new ArrayList<>();
    public static List<Response> responseList = new ArrayList<>();
    public static int activeIndex = -1;

    //Request building functions
    public static void createNewRequest() {
        RequestSpecification reqSpec = SerenityRest.given();
        reqSpecList.add(reqSpec);
        activeIndex++;
    }

    public static void setBaseURI(String URI) {
        reqSpecList.get(activeIndex).baseUri(URI);
    }

    public static void setBasePath(String path) {
        reqSpecList.get(activeIndex).basePath(path);
    }

    public static void addHeader(String headerName, String headerValue) {
        reqSpecList.get(activeIndex).header(headerName, headerValue);
    }

    public static void addQueryParam(String paramName, String paramValue) {
        reqSpecList.get(activeIndex).queryParam(paramName, paramValue);
    }

    public static void setContentType(String contentType) {
        reqSpecList.get(activeIndex).contentType(contentType);
    }

    public static void setRelaxedHttpsValidation() {
        reqSpecList.get(activeIndex).relaxedHTTPSValidation();
    }

    public static void setUrlEncoding(boolean encodingStatus) {
        reqSpecList.get(activeIndex).urlEncodingEnabled(encodingStatus);
    }

    public static void makeRequest(String reqType) {
        Response response = null;
        switch (reqType.toLowerCase()) {
            case "get" -> response = reqSpecList.get(activeIndex)
                    .log().all()
                    .when().get();
            case "post" -> response = reqSpecList.get(activeIndex)
                    .log().all()
                    .when().post();
            case "put" -> response = reqSpecList.get(activeIndex)
                    .log().all()
                    .when().put();
            case "patch" -> response = reqSpecList.get(activeIndex)
                    .log().all()
                    .when().patch();
            case "delete" -> response = reqSpecList.get(activeIndex)
                    .log().all()
                    .when().delete();
            default -> {
                logger.info("No such request type");
                Assert.fail("No such request type");
            }
        }
        logger.info("\n\nResponse Body:\n" + response.getBody().asPrettyString());
        responseList.add(response);
    }

    //Validation Functions
    public static void verifyKeyAndValuePresence(String pathProvided, String expectedValue, Response... responses) {
        for (Response res : responses) {
            Object valueFoundAtPath = res.jsonPath().getString(pathProvided);
            if (!StringUtils.equals(expectedValue, String.valueOf(valueFoundAtPath)))
                if (pathProvided.split("\\.").length == 1) {
                    logger.info("Couldn't find the specified value for Key: " + pathProvided);
                    Assert.fail("Couldn't find the specified value for Key: " + pathProvided);
                } else {
                    logger.info("Couldn't find the specified value for path: " + pathProvided);
                    Assert.fail("Couldn't find the specified value for path: " + pathProvided);
                }
            if (pathProvided.split("\\.").length == 1)
                logger.info("Successfully verified presence of " + expectedValue + " in response for provided Key: \"" + pathProvided + "\"");
            else
                logger.info("Successfully verified presence of " + expectedValue + " in response for provided path: \"" + pathProvided + "\"");
        }
    }

    public static void verifyResponseSchema(String schemaName, Response... responses) {
        for (Response res : responses)
            res.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\schemas\\" + schemaName + ".json")));
        logger.info("Response Schema verified successfully.");
    }

    public static void verifyJsonArraySize(String pathProvided, int size, Response... responses) {
        for (Response res : responses) {
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
            } else if (valueFoundAtPath instanceof ArrayList arr)
                arrSize = arr.size();
            else {
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

    public static void verifyResponseTime(long expectedTimeInSeconds, Response... responses) {
        for (Response res : responses) {
            if (expectedTimeInSeconds > 5) {
                expectedTimeInSeconds = 5;
                logger.info("Expected time is much more. Trimming it down to 5 seconds");
            }
            long actualTime = res.getTime();
            logger.info("Actual Time: " + actualTime + " milliseconds and expected time is: " + expectedTimeInSeconds * 1000 + " milliseconds");
            Assert.assertTrue(actualTime < expectedTimeInSeconds * 1000);
        }
    }

    public static void messageValidation(String expectedMessage, Response... responses) {
        for (Response res : responses) {
            String responseMessage = res.body().asPrettyString();
            if (responseMessage.contains(expectedMessage)) {
                logger.info("Successfully found " + expectedMessage + " is Response body", true);
            } else {
                logger.info("Unable to find " + expectedMessage + " is Response body");
                Assert.fail("Unable to find " + expectedMessage + " is Response body");
            }
        }
    }

    public static void verifyStatusCode(int statusCode, Response... responses) {
        for (Response res : responses) {
            Assert.assertEquals("Status code didn't match", res.getStatusCode(), statusCode);
            logger.info("Status code verified");
        }
    }

    public static void verifyValueInJsonArray(String pathProvided, String expectedValue, Response... responses) {
        for (Response res : responses) {
            Object valueFoundAtPath = res.jsonPath().get(pathProvided);
            if (ObjectUtils.allNull(valueFoundAtPath)) {
                logger.info("No value found for for provided key: \"" + pathProvided + "\"");
                Assert.fail("No value found for for provided key: \"" + pathProvided + "\"");
            } else if (valueFoundAtPath instanceof ArrayList arr) {
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
