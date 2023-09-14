package common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.PathNotFoundException;
import io.cucumber.datatable.DataTable;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import logging.EnableJLCLogging;
import logging.EnableSlf4jLogging;
import logging.LoggerUtils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import reporting.EnableGemReporting;
import reporting.EnableSerenityReporting;
import reporting.Reporting;
import stepdefinitions.UtilStepDefinition;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class CommonFunctions {
    private LoggerUtils logger = logger();
    public Reporting reporting = reporting();

    public HashMap<String, RequestSpecification> reqSpecMap = new HashMap<>();
    public HashMap<String, Response> responseMap = new HashMap<>();
    public String latestResponseKey = "";
    static final EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    static HashMap<String, String> extractedValue = new HashMap<>();
    final String environment = System.getProperty("environment");

    // Comparator variables
    private static Map<String, Object> mismatchedFinal = new HashMap<>();
    private static List<String> mismatchInJson1Keys = new ArrayList<>();
    private static List<String> mismatchInJson2Keys = new ArrayList<>();
    private static HashSet<String> mismatchInJson1Values = new HashSet<>();
    private static HashSet<String> mismatchInJson2Values = new HashSet<>();
    private static HashSet<String> mismatchComments = new HashSet<>();
    private static String expectedJson = "";
    private static String res = "";
    private static HashSet<String> finalResult = new HashSet<>();

    /**
     * Function to reset all the declared variables before execution
     */
    public void resetVariables() {
        reqSpecMap = new HashMap<>();
        responseMap = new HashMap<>();
        latestResponseKey = "";
        extractedValue = new HashMap<>();
        mismatchedFinal = new HashMap<>();
        mismatchInJson1Keys = new ArrayList<>();
        mismatchInJson2Keys = new ArrayList<>();
        mismatchInJson1Values = new HashSet<>();
        mismatchInJson2Values = new HashSet<>();
        mismatchComments = new HashSet<>();
        expectedJson = "";
        res = "";
        finalResult = new HashSet<>();
    }

    /**
     * Function implementing reporting interface
     */

    public Reporting reporting() {
        Properties properties;
        try {
            properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            String reportingType = properties.getProperty("ReportingType");
            if (null == reportingType) {
                reporting = new EnableSerenityReporting();
            } else if (StringUtils.equalsIgnoreCase(reportingType.toLowerCase(), "gemjar")) {
                reporting = new EnableGemReporting();
            } else if (StringUtils.equalsIgnoreCase(reportingType.toLowerCase(), "serenity")) {
                reporting = new EnableSerenityReporting();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reporting;
    }

    /**
     * Function implementing logging interface
     */
    public LoggerUtils logger() {
        Properties properties;
        try {
            properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            String loggingType = properties.getProperty("Logger");
            if (null == loggingType) {
                logger = new EnableSlf4jLogging();
            } else if (StringUtils.equalsIgnoreCase(loggingType.toLowerCase(), "jlc")) {
                logger = new EnableJLCLogging();
            } else if (StringUtils.equalsIgnoreCase(loggingType.toLowerCase(), "slf4j")) {
                logger = new EnableSlf4jLogging();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logger;
    }

    /**
     * Creates a new RequestSpecification for the user to modify and use as and when required
     * <p>This is the starting point for all new requests and must be called for successful Rest request.
     *
     * @param key      The key name with which the RequestSpecification is stored is mapped.
     * @param endpoint The complete endpoint which will be hit during request
     */
    public void createsRequest(String key, String endpoint) {
        RequestSpecification reqSpec = SerenityRest.given().spec(requestSpecifications(endpoint));
        if (!reqSpecMap.containsKey(key)) {
            latestResponseKey = key;
            reqSpecMap.put(key, reqSpec);
            reporting.reportSteps("Create Request", "PASS: Successfully created request");
        } else {
            logger.logError("A request with this key name already exists. Please rename either request. Request to be renamed: " + key);
            Assert.fail("A request with this key name already exists. Please rename either request. Request to be renamed: " + key);
        }
    }

    /**
     * Initiates a request based on the latest RequestSpecification.<p>
     * The Response is then saved into another Hashmap, with the same key as the RequestSpecification that created it.
     *
     * @param requestType Request Method to be sent. Can be GET, POST, PUT, PATCH, DELETE
     */
    public void sendRequest(String requestType) {
        requestType = requestType.toUpperCase();
        try {
            Response response = reqSpecMap.get(latestResponseKey).log().all().when().request(requestType);
            logger.logInfo("Response Body: \n\n" + response.getBody().asPrettyString());
            responseMap.put(latestResponseKey, response);
            reporting.reportSteps("Send Request", "PASS: Successfully sent request");
        } catch (Exception e) {
            logger.logInfo("Unsupported method: " + requestType);
        }
    }


    /**
     * Initiates a request based on the latest RequestSpecification.<p>
     * The Response is then saved into another Hashmap, with the same key as the RequestSpecification that created it.
     *
     * @param requestType Request Method to be sent. Can be GET, POST, PUT, PATCH, DELETE
     * @param requestKey  Key name of the request associated with this response
     */
    public void sendRequest(String requestType, String requestKey) {
        Response response = null;
        requestType = requestType.toUpperCase();
        try {
            response = reqSpecMap.get(requestKey).log().all().when().request(requestType);
            reporting.reportSteps("Send Request", "PASS: Successfully sent request");
        } catch (Exception e) {
            logger.logInfo("Unsupported method: " + requestType);
        }
        assert null != response;
        logger.logInfo("Response Body: \n\n" + response.getBody().asPrettyString());
        responseMap.put(requestKey, response);
    }

    /**
     * Function to add method body to request created
     *
     * @param methodBody Key of the method body under which request body is stored in data.json file
     */
    public void addBody(String methodBody) {
        reqSpecMap.get(latestResponseKey).log().all().when().body(jsonBody(methodBody, environment));
    }

    /**
     * Function to add method body to request created
     *
     * @param methodBody Key of the method body under which request body is stored in data.json file
     * @param requestKey Key name of the request associated with this response
     */
    public void addBody(String methodBody, String requestKey) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        reqSpecMap.get(requestKey).when().body(jsonBody(methodBody, environment));
    }

    /**
     * Sets the ContentType of the latest RequestSpecification
     *
     * @param contentType The Content-Type to be set
     */
    public void setContentType(String contentType) {
        reqSpecMap.get(latestResponseKey).contentType(contentType);
    }

    /**
     * Sets the ContentType of the specified RequestSpecification
     *
     * @param contentType The Content-Type to be set
     * @param requestKey  Key name of the request associated with this response
     */
    public void setContentType(String contentType, String requestKey) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        reqSpecMap.get(requestKey).contentType(contentType);
    }


    /**
     * Verifies if the status code of the latest response matches expectations
     *
     * @param statusCode Expected Status Code of the Response.
     */
    public void verifyStatusCode(int statusCode) {
        Assert.assertEquals("Status Code mismatch", responseMap.get(latestResponseKey).getStatusCode(), statusCode);
        logger.logInfo("Status code verified");
    }

    /**
     * Verifies if the status code of the specified response matches expectations
     *
     * @param statusCode  Expected Status Code of the Response.
     * @param responseKey Key name of the request associated with this response.
     */
    public void verifyStatusCode(int statusCode, String responseKey) {
        if (!responseMap.containsKey(responseKey)) {
            logger.logError("No such request found: " + responseKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + responseKey + ". Kindly re-check the Request Name.");
        }
        Response res = responseMap.get(responseKey);
        Assert.assertEquals("Status Code mismatch", statusCode, res.getStatusCode());
        logger.logInfo("Status code verified");
    }

    /**
     * Add a single header to the latest request specification
     *
     * @param headerName  Header Name
     * @param headerValue Header Value
     */

    public void addHeaders(String headerName, String headerValue) {
        reqSpecMap.get(latestResponseKey).header(headerName, headerValue);
    }

    /**
     * Add a single header to the specified request specification
     *
     * @param headerName  Header Name
     * @param headerValue Header Value
     * @param requestKey  Key name of the request associated with this response
     */
    public void addHeaders(String requestKey, String headerName, String headerValue) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        reqSpecMap.get(requestKey).header(headerName, headerValue);
    }

    /**
     * Add multiple headers to the latest request specification via DataTable
     *
     * @param dt DataTable of Header Name and Header Value
     */
    public void addHeaders(DataTable dt) {
        if (validateHeader(dt, "Header Name", "Header Value")) {
            List<Map<String, String>> headers = dt.asMaps();
            for (Map<String, String> header : headers) {
                reqSpecMap.get(latestResponseKey).header(header.get("Header Name"), header.get("Header Value"));
            }
        }
    }

    /**
     * Add multiple headers to the specified request specification via DataTable
     *
     * @param dt         DataTable of Header Name and Header Value
     * @param requestKey Key name of the request associated with this response
     */
    public void addHeaders(String requestKey, DataTable dt) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        if (validateHeader(dt, "Header Name", "Header Value")) {
            List<Map<String, String>> headers = dt.asMaps();
            for (Map<String, String> header : headers) {
                reqSpecMap.get(requestKey).header(header.get("Header Name"), header.get("Header Value"));
            }
        }
    }

    /**
     * Add multiple headers to the latest request specification via a Map
     *
     * @param headers Headers as a Map of header key and header value
     */

    public void addHeadersViaMap(Map<String, String> headers) {
        reqSpecMap.get(latestResponseKey).headers(headers);
    }

    /**
     * Add multiple headers to the specified request specification via a Map
     *
     * @param headers    Headers as a Map of header key and header value
     * @param requestKey Key name of the request associated with this response
     */

    public void addHeadersViaMap(String requestKey, Map<String, String> headers) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        reqSpecMap.get(requestKey).headers(headers);
    }

    /**
     * Add a Query Parameter to the latest request specification
     *
     * @param parameterKey   Header Name
     * @param parameterValue Header Value
     */

    public void addQueryParams(String parameterKey, String parameterValue) {
        reqSpecMap.get(latestResponseKey).queryParam(parameterKey, parameterValue);
    }

    /**
     * Add a Query Parameter to the specified request specification
     *
     * @param parameterKey   Query Param Name
     * @param parameterValue Query Param Value
     * @param requestKey     Key name of the request associated with this response
     */

    public void addQueryParams(String requestKey, String parameterKey, String parameterValue) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }

        reqSpecMap.get(requestKey).queryParam(parameterKey, parameterValue);
    }

    /**
     * Add multiple Query Parameters to the latest RequestSpecification via a DataTable
     *
     * @param dt DataTable of Parameter Key and Value
     */
    public void addQueryParams(DataTable dt) {
        if (validateHeader(dt, "Parameter Key", "Parameter Value")) {
            List<Map<String, String>> queryParams = dt.asMaps();
            for (Map<String, String> params : queryParams) {
                reqSpecMap.get(latestResponseKey).queryParam(params.get("Parameter Key"), params.get("Parameter Value"));
            }
        }
    }

    /**
     * Add multiple Query Parameters to the specified RequestSpecification via a DataTable
     *
     * @param dt         DataTable of Parameter Key and Value
     * @param requestKey Key name of the request associated with this response
     */
    public void addQueryParams(String requestKey, DataTable dt) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        if (validateHeader(dt, "Parameter Key", "Parameter Value")) {
            List<Map<String, String>> queryParams = dt.asMaps();
            for (Map<String, String> params : queryParams) {
                reqSpecMap.get(requestKey).queryParam(params.get("Parameter Key"), params.get("Parameter Value"));
            }
        }
    }

    /**
     * Add multiple Query Parameters to latest request specification via a Map
     *
     * @param queryParam Query Params as a Map of param key and param value
     */
    public void addQueryParamsViaMap(Map<String, String> queryParam) {
        reqSpecMap.get(latestResponseKey).queryParams(queryParam);
    }

    /**
     * Add multiple Query Parameters to the specified request specification via a Map
     *
     * @param queryParam Query Params as a Map of param key and param value
     * @param requestKey Key name of the request associated with this response
     */
    public void addQueryParamsViaMap(String requestKey, Map<String, String> queryParam) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        reqSpecMap.get(requestKey).queryParams(queryParam);
    }

    /**
     * Add a Form Parameter to the latest request specification
     *
     * @param parameterKey   Form Param Name
     * @param parameterValue Form Param Value
     */
    public void addFormParams(String parameterKey, String parameterValue) {
        reqSpecMap.get(latestResponseKey).queryParam(parameterKey, parameterValue);
    }

    /**
     * Add a Form Parameter to the specified request specification
     *
     * @param parameterKey   Form Param Name
     * @param parameterValue Form Param Value
     * @param requestKey     Key name of the request associated with this response
     */
    public void addFormParams(String requestKey, String parameterKey, String parameterValue) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }

        reqSpecMap.get(requestKey).formParam(parameterKey, parameterValue);
    }

    /**
     * Add multiple Form Parameters to the latest RequestSpecification via a DataTable
     *
     * @param dt DataTable of Parameter Key and Value
     */
    public void addFormParams(DataTable dt) {
        if (validateHeader(dt, "Parameter Key", "Parameter Value")) {
            List<Map<String, String>> formParams = dt.asMaps();
            for (Map<String, String> params : formParams) {
                reqSpecMap.get(latestResponseKey).formParam(params.get("Parameter Key"), params.get("Parameter Value"));
            }
        }
    }

    /**
     * Add multiple Form Parameters to the specified RequestSpecification via a DataTable
     *
     * @param dt         DataTable of Parameter Key and Value
     * @param requestKey Key name of the request associated with this response
     */
    public void addFormParams(String requestKey, DataTable dt) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        if (validateHeader(dt, "Parameter Key", "Parameter Value")) {
            List<Map<String, String>> formParams = dt.asMaps();
            for (Map<String, String> params : formParams) {
                reqSpecMap.get(requestKey).formParam(params.get("Parameter Key"), params.get("Parameter Value"));
            }
        }
    }

    /**
     * Add multiple Form Parameters to the latest request specification via a Map
     *
     * @param formParams Form Params as a Map of param key and param value
     */
    public void addFormParamsViaMap(Map<String, String> formParams) {
        reqSpecMap.get(latestResponseKey).formParams(formParams);
    }

    /**
     * Add multiple Form Parameters to the specified request specification via a Map
     *
     * @param formParams Form Params as a Map of param key and param value
     * @param requestKey Key name of the request associated with this response
     */
    public void addFormParamsViaMap(String requestKey, Map<String, String> formParams) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        reqSpecMap.get(requestKey).formParams(formParams);
    }


    /**
     * Add a Path Parameter to the latest request specification
     *
     * @param parameterKey   Path Param Name
     * @param parameterValue Path Param Value
     */
    public void addPathParams(String parameterKey, String parameterValue) {

        reqSpecMap.get(latestResponseKey).pathParam(parameterKey, parameterValue);
    }

    /**
     * Add a Path Parameter to the specified request specification
     *
     * @param parameterKey   Path Param Name
     * @param parameterValue Path Param Value
     * @param requestKey     Key name of the request associated with this response
     */

    public void addPathParams(String requestKey, String parameterKey, String parameterValue) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        reqSpecMap.get(requestKey).pathParam(parameterKey, parameterValue);
    }

    /**
     * Add Path Parameters to the latest RequestSpecification via a DataTable
     *
     * @param dt DataTable of Parameter Key and Value
     */

    public void addPathParams(DataTable dt) {
        if (validateHeader(dt, "Parameter Key", "Parameter Value")) {
            List<Map<String, String>> pathParams = dt.asMaps();
            for (Map<String, String> params : pathParams) {
                reqSpecMap.get(latestResponseKey).pathParam(params.get("Parameter Key"), params.get("Parameter Value"));
            }
        }
    }

    /**
     * Add Path Parameters to the specified RequestSpecification via a DataTable
     *
     * @param dt         DataTable of Parameter Key and Value
     * @param requestKey Key name of the request associated with this response
     */
    public void addPathParams(String requestKey, DataTable dt) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        if (validateHeader(dt, "Parameter Key", "Parameter Value")) {
            List<Map<String, String>> pathParams = dt.asMaps();
            for (Map<String, String> params : pathParams) {
                reqSpecMap.get(requestKey).pathParam(params.get("Parameter Key"), params.get("Parameter Value"));
            }
        }

    }

    /**
     * Add multiple Path Parameters to the latest request specification via Map
     *
     * @param pathParams Path Params as a Map of param key and param value
     */
    public void addPathParamsViaMap(Map<String, String> pathParams) {
        reqSpecMap.get(latestResponseKey).pathParams(pathParams);
    }

    /**
     * Add multiple Path Parameters to the specified request specification via Map
     *
     * @param pathParams Path Params as a Map of param key and param value
     * @param requestKey Key name of the request associated with this response
     */

    public void addPathParamsViaMap(String requestKey, Map<String, String> pathParams) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        reqSpecMap.get(requestKey).pathParams(pathParams);
    }

    /**
     * Extract value of a key from the latest response
     *
     * @param key The key whose value needs to get logged
     */

    public void extractValue(String key) {
        extractedValue.put(key, responseMap.get(latestResponseKey).jsonPath().getString(key));
    }

    public void extractValues(DataTable keys) {
        List<Map<String, String>> keyMaps = keys.asMaps();
        if (validateHeader(keys, "Key Path")) {
            for (Map<String, String> keyMap : keyMaps) {
                String key = keyMap.get("Key Path");
                extractedValue.put(key, responseMap.get(latestResponseKey).jsonPath().getString(key));
            }
        } else {
            logger.logError("DataTable column 'Key Path' not found or empty.");
        }
    }

    /**
     * Function to validate header name of the datatable passed from the feature file
     *
     * @param dataTable       dataTable which is passed from feature file
     * @param expectedHeaders List of expected header nams
     */
    public boolean validateHeader(DataTable dataTable, String... expectedHeaders) {
        boolean allHeadersFound = true;
        for (String expectedHeader : expectedHeaders) {
            List<String> actualHeaders = dataTable.row(0);
            if (!actualHeaders.contains(expectedHeader)) {
                logger.logError("Expected header: " + expectedHeader + "Not Found.");
                allHeadersFound = false;
                break;
            }
        }
        return allHeadersFound;
    }


    public void extractValues(DataTable keys, String requestKey) {
        if (!responseMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            return;
        }
        List<Map<String, String>> keyMaps = keys.asMaps();
        if (validateHeader(keys, "Key Path")) {
            for (Map<String, String> keyMap : keyMaps) {
                String key = keyMap.get("Key Path");
                extractedValue.put(key, responseMap.get(latestResponseKey).jsonPath().getString(key));
            }
        } else {
            logger.logError("DataTable column 'Key Path' not found or empty.");
        }
    }


    /**
     * Extract value of a key from the specified response
     *
     * @param key        The key whose value needs to get logged
     * @param requestKey The Response which needs to be referred to for key-value extraction
     */

    public void extractValue(String key, String requestKey) {
        if (!responseMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }
        extractedValue.put(key, responseMap.get(requestKey).jsonPath().getString(key));
    }

    /**
     * Adds the extracted key value from another response body to the request body the latest request
     *
     * @param newKeyPath JsonPath of the Key which needs to be added in method body
     * @param keyToAdd   Name of the Key which needs to be added
     * @param methodBody Method body in which key needs to be added
     */

    public void addExtractedValueToRequest(String keyToAdd, String newKeyPath, String methodBody) {
        reqSpecMap.get(latestResponseKey).log().all().when().body(jsonBody(methodBody, environment, newKeyPath, extractedValue.get(keyToAdd), true));
    }

    /**
     * Adds the extracted key value from another response body to the request body the specified request
     *
     * @param newKeyPath JsonPath of the Key which needs to be added in method body
     * @param keyToAdd   Name of the Key which needs to be added
     * @param methodBody Method body in which key needs to be added
     */

    public void addExtractedValueToRequest(String keyToAdd, String newKeyPath, String methodBody, String requestKey) {
        reqSpecMap.get(requestKey).log().all().when().body(jsonBody(methodBody, environment, newKeyPath, extractedValue.get(keyToAdd), true));
    }

    /**
     * This function creates and returns the request specifications with a set endpoint.
     *
     * @param endpoint The endpoint at which request will get hit
     */
    public RequestSpecification requestSpecifications(String endpoint) {
        RequestSpecification req = SerenityRest.given();
        String baseUri = EnvironmentSpecificConfiguration.from(variables).getProperty(endpoint);

        // Check if query parameters are present in the endpoint
        if (baseUri.contains("?")) {
            // Add query parameters if present in the endpoint
            String[] paramArray = baseUri.split("\\?")[1].split("&");
            for (String param : paramArray) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String paramName = keyValue[0].trim();
                    String paramValue = keyValue[1].trim();
                    req.queryParam(paramName, paramValue);
                }
            }
        }

        // Set Base URI after trimming Query Params
        int queryParamIndex = baseUri.indexOf("?");
        if (queryParamIndex != -1) {
            baseUri = baseUri.substring(0, queryParamIndex);
        }

        req = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .build();
        return req;
    }

    /**
     * Verifies if the latest response contains the specified string in the response body
     *
     * @param expectedMessage Expected Message to be found in Response Body.
     */
    public void verifyPresenceOfMessage(String expectedMessage) {
        Response res = responseMap.get(latestResponseKey);
        String responseMessage = res.body().asPrettyString();
        if (responseMessage.contains(expectedMessage)) {
            logger.logInfo("Successfully found " + expectedMessage + " is Response body", true);
        } else {
            logger.logError("Unable to find \"" + expectedMessage + "\" in Response body");
            Assert.fail("Unable to find \"" + expectedMessage + "\" in Response body");
        }
    }

    /**
     * Verifies if the specified response contains the specified string in the response body
     *
     * @param expectedMessage Expected Message to be found in Response Body.
     * @param responseKey     Key name of the request associated with this response.
     */
    public void verifyPresenceOfMessage(String expectedMessage, String responseKey) {
        if (!responseMap.containsKey(responseKey)) {
            logger.logError("No such response found. Please recheck the provided Response Key: " + responseKey);
            Assert.fail("No such response found. Please recheck the provided Response Key: " + responseKey);
        } else {
            Response res = responseMap.get(responseKey);
            String responseMessage = res.body().asPrettyString();
            if (responseMessage.contains(expectedMessage)) {
                logger.logInfo("Successfully found " + expectedMessage + " is Response body", true);
            } else {
                logger.logError("Unable to find \"" + expectedMessage + "\" in Response body");
                Assert.fail("Unable to find \"" + expectedMessage + "\" in Response body");
            }
        }
    }

    /**
     * Verifies if the latest response was returned within the specified time limit or not.
     *
     * @param expectedTime Maximum amount of time the API is expected to take.
     */
    public void verifyResponseTime(String expectedTime) {
        try {
            float expectedTimeInSeconds = Float.parseFloat(expectedTime);
            Response res = responseMap.get(latestResponseKey);
            long actualTime = res.getTime();
            logger.logInfo("Actual Time: " + actualTime + " milliseconds and expected time is: " + expectedTimeInSeconds * 1000 + " milliseconds");
            Assert.assertTrue("Response took longer than expected. Expected Time: At least " + expectedTimeInSeconds * 1000 + " milliseconds. Actual time: " + actualTime + " milliseconds", actualTime < expectedTimeInSeconds * 1000);
        } catch (NumberFormatException e) {
            logger.logError("Invalid time format. Please recheck.");
            Assert.fail("Invalid time format. Please recheck.");
        }
    }

    /**
     * Verifies if the specified response was returned within the specified time limit or not.
     *
     * @param expectedTime Maximum amount of time the API is expected to take.
     * @param responseKey  Key name of the request associated with this response.
     */
    public void verifyResponseTime(String expectedTime, String responseKey) {
        if (!responseMap.containsKey(responseKey)) {
            logger.logError("No such response found. Please recheck the provided Response Key: " + responseKey);
            Assert.fail("No such response found. Please recheck the provided Response Key: " + responseKey);
            return;
        }
        try {
            float expectedTimeInSeconds = Float.parseFloat(expectedTime);
            Response res = responseMap.get(latestResponseKey);
            long actualTime = res.getTime();
            logger.logInfo("Actual Time: " + actualTime + " milliseconds and expected time is: " + expectedTimeInSeconds * 1000 + " milliseconds");
            Assert.assertTrue("Response took longer than expected. Expected Time: At least " + expectedTimeInSeconds * 1000 + " milliseconds. Actual time: " + actualTime + " milliseconds", actualTime < expectedTimeInSeconds * 1000);
        } catch (NumberFormatException e) {
            logger.logError("Invalid time format. Please recheck.");
            Assert.fail("Invalid time format. Please recheck.");
        }
    }

    /**
     * Verifies whether the length of the specified JSONArray (Using JsonPath) matches expectations in the latest response
     * <p>
     * Uses JsonPath to search for the value and verifies if it is a JSONArray or not beforehand.
     *
     * @param pathProvided JSON Path of the expected key.
     * @param size         Expected size of the JSONArray found when traversing the JSON Path provided.
     */

    public void verifyJsonArraySize(String pathProvided, String size) {
        Response res = responseMap.get(latestResponseKey);
        int arrSize = -1;
        Object valueFoundAtPath = res.jsonPath().get(pathProvided);

        try {
            int expectedSize = Integer.parseInt(size);

            if (ObjectUtils.allNull(valueFoundAtPath)) {
                if (expectedSize == 0) {
                    logger.logInfo("No array found for specified key: " + pathProvided + ". Size 0 verified.");
                    return;
                } else {
                    logger.logError("No value found for provided key: \"" + pathProvided + "\"");
                    Assert.fail("No value found for provided key: \"" + pathProvided + "\"");
                }
            } else if (valueFoundAtPath instanceof ArrayList) {
                ArrayList arr = (ArrayList) valueFoundAtPath;
                arrSize = arr.size();
            } else {
                logger.logError("Value of provided key: " + pathProvided + " is not a JSONArray. Can't validate the size");
                Assert.fail("Value of provided key: " + pathProvided + " is not a JSONArray. Can't validate the size");
            }

            if (arrSize == expectedSize) {
                logger.logInfo("The array with size " + expectedSize + " exists");
            } else {
                logger.logError("The array with size " + expectedSize + " does not exist");
                Assert.fail("The array with size " + expectedSize + " does not exist");
            }
        } catch (NumberFormatException e) {
            logger.logError("Invalid size format. Please recheck.");
            Assert.fail("Invalid size format. Please recheck.");
        }
    }

    /**
     * Verifies whether the length of the specified JSONArray (Using JsonPath) matches expectations in the specified response
     * <p>
     * Uses JsonPath to search for the value and verifies if it is a JSONArray or not beforehand.
     *
     * @param pathProvided JSON Path of the expected key.
     * @param size         Expected size of the JSONArray found when traversing the JSON Path provided.
     * @param responseKey  Key name of the request associated with this response.
     */
    public void verifyJsonArraySize(String pathProvided, int size, String responseKey) {
        if (!responseMap.containsKey(responseKey)) {
            logger.logError("No such request found: " + responseKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + responseKey + ". Kindly re-check the Request Name.");
        }
        try {
            if (!responseMap.containsKey(responseKey)) {
                logger.logError("No such response found. Please recheck the provided Response Key: " + responseKey);
                Assert.fail("No such response found. Please recheck the provided Response Key: " + responseKey);
            } else {
                Response res = responseMap.get(responseKey);
                int arrSize = -1;
                Object valueFoundAtPath = res.jsonPath().get(pathProvided);
                if (ObjectUtils.allNull(valueFoundAtPath)) {
                    if (size == 0) {
                        logger.logInfo("No array found for specified key: " + pathProvided + ". Size 0 verified.");
                        return;
                    } else {
                        logger.logError("No value found for for provided key: \"" + pathProvided + "\"");
                        Assert.fail("No value found for for provided key: \"" + pathProvided + "\"");
                    }
                } else if (valueFoundAtPath instanceof ArrayList) {
                    ArrayList arr = (ArrayList) valueFoundAtPath;
                    arrSize = arr.size();
                } else {
                    logger.logError("Value of provided key: " + pathProvided + " is not a JSONArray. Can't validate the size");
                    Assert.fail("Value of provided key: " + pathProvided + " is not a JSONArray. Can't validate the size");
                }

                if (arrSize == size) {
                    logger.logInfo("The array with size " + size + " exists");
                } else {
                    logger.logError("The array with size " + size + " does not exist");
                    Assert.fail("The array with size " + size + " does not exist");
                }
            }

        } catch (Exception e) {
            logger.logError("Invalid size format. Please recheck.");
            Assert.fail("Invalid size format. Please recheck.");
        }
    }

    /**
     * Validate presence of key-value pair in the latest response body for a given JsonPath
     *
     * @param verificationData DataTable containing JsonPath of Key , the corresponding operation, and Expected Value
     */
    public void verifyKeyValueInResponseBody(DataTable verificationData) {
        Response res = responseMap.get(latestResponseKey);
        JsonPath jsonPath = res.jsonPath();
        List<Map<String, String>> verificationList = verificationData.asMaps();
        if (validateHeader(verificationData, "Key", "operation", "Value")) {
            for (Map<String, String> verification : verificationList) {
                String key = verification.get("Key");
                String operation = verification.get("operation");
                String expectedValue = verification.get("Value");

                String actualValue = null;
                try {
                    actualValue = jsonPath.getString(key);
                    Assert.assertNotNull("The value of the key specified by Json Path: " + key + " was null", actualValue);
                } catch (PathNotFoundException e) {
                    logger.logError("The Key specified by Json Path: " + key + " was not found");
                    Assert.fail("The Key specified by Json Path: " + key + " was not found");
                }

                switch (operation.toLowerCase()) {
                    case "equals":
                        Assert.assertEquals("Value for key '" + key + "' is not as expected", expectedValue, actualValue);
                        logger.logInfo("Value for key '" + key + "' is as expected: " + expectedValue);
                        break;
                    case "not equals":
                        Assert.assertNotEquals("Value for key '" + key + "' was found to be equal to : " + expectedValue, expectedValue, actualValue);
                        logger.logInfo("Value for key '" + key + "' does not equal: " + expectedValue);
                        break;
                    case "contains":
                        Assert.assertTrue("Value for key '" + key + "' does not contain: " + expectedValue, actualValue.contains(expectedValue));
                        logger.logInfo("Value for key '" + key + "' contains: " + expectedValue);
                        break;
                    case "not contains":
                        Assert.assertFalse("Value for key '" + key + "' contains: " + expectedValue, actualValue.contains(expectedValue));
                        logger.logInfo("Value for key '" + key + "' does not contain: " + expectedValue);
                        break;
                    default:
                        logger.logInfo("Unsupported operation: " + operation);
                        Assert.fail("Unsupported operation: " + operation);
                }
            }
        }
    }

    /**
     * Validate presence of key-value pair in the specified response body for a given JsonPath
     *
     * @param verificationData DataTable containing JsonPath of Key ,operation and Expected Value
     * @param responseKey      Key name of the request associated with this response.
     */

    public void verifyKeyValueInResponseBody(DataTable verificationData, String responseKey) {
        if (!responseMap.containsKey(responseKey)) {
            logger.logError("No such response found. Please recheck the provided Response Key: " + responseKey);
            Assert.fail("No such response found. Please recheck the provided Response Key: " + responseKey);
        }
        Response res = responseMap.get(responseKey);
        JsonPath jsonPath = res.jsonPath();
        List<Map<String, String>> verificationList = verificationData.asMaps();
        if (validateHeader(verificationData, "Key", "operation", "Value")) {

            for (Map<String, String> verification : verificationList) {
                String key = verification.get("Key");
                String operation = verification.get("operation");
                String expectedValue = verification.get("Value");

                String actualValue = null;
                try {
                    actualValue = jsonPath.getString(key);
                    Assert.assertNotNull("The value of the key specified by Json Path: " + key + " was null", actualValue);
                } catch (PathNotFoundException e) {
                    logger.logError("The Key specified by Json Path: " + key + " was not found");
                    Assert.fail("The Key specified by Json Path: " + key + " was not found");
                }

                switch (operation.toLowerCase()) {
                    case "equals":
                        Assert.assertEquals("Value for key '" + key + "' is not as expected", expectedValue, actualValue);
                        logger.logInfo("Value for key '" + key + "' is as expected: " + expectedValue);
                        break;
                    case "not equals":
                        Assert.assertNotEquals("Value for key '" + key + "' was found to be equal to : " + expectedValue, expectedValue, actualValue);
                        logger.logInfo("Value for key '" + key + "' does not equal: " + expectedValue);
                        break;
                    case "contains":
                        Assert.assertTrue("Value for key '" + key + "' does not contain: " + expectedValue, actualValue.contains(expectedValue));
                        logger.logInfo("Value for key '" + key + "' contains: " + expectedValue);
                        break;
                    case "not contains":
                        Assert.assertFalse("Value for key '" + key + "' contains: " + expectedValue, actualValue.contains(expectedValue));
                        logger.logInfo("Value for key '" + key + "' does not contain: " + expectedValue);
                        break;
                    default:
                        logger.logInfo("Unsupported operation: " + operation);
                        Assert.fail("Unsupported operation: " + operation);
                }
            }
        }
    }

    /**
     * Verifies presence of a particular element in a JSONArray in the latest response
     * <p>
     * Uses JsonPath to search for the value and verifies if it is a JSONArray or not beforehand.
     * <p>
     * Does not work with Array of JSONObjects as they will have an extended JsonPath.
     *
     * @param pathProvided  JSON Path of the parent JSON Array.
     * @param expectedValue Value of item expected to be found in the JSON Array found using the provided JSON Path.
     */
    public void verifyValueInJsonArray(String pathProvided, String expectedValue) {
        Response res = responseMap.get(latestResponseKey);
        Object valueFoundAtPath = res.jsonPath().get(pathProvided);
        if (ObjectUtils.allNull(valueFoundAtPath)) {
            logger.logError("No value found for for provided key: \"" + pathProvided + "\"");
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
                    logger.logError("Couldn't find " + expectedValue + " in response for provided key: \"" + pathProvided + "\"");
                    Assert.fail("Couldn't find " + expectedValue + " in response for provided key: \"" + pathProvided + "\"");
                }
            } else {
                logger.logError("Value for provided key: " + pathProvided + " is a JSONArray of JSONObjects. Can't validate the value");
                Assert.fail("Value for provided key: " + pathProvided + " is a JSONArray of JSONObjects. Can't validate the value");
            }
        } else {
            logger.logError("Value for provided key: " + pathProvided + " is not a JSONArray. Can't validate the value");
            Assert.fail("Value for provided key: " + pathProvided + " is not a JSONArray. Can't validate the value");
        }
        if (pathProvided.split("\\.").length == 1)
            logger.logInfo("Successfully verified presence of " + expectedValue + " in response for provided key: \"" + pathProvided + "\"");
        else
            logger.logInfo("Successfully verified presence of " + expectedValue + " in response for provided path: \"" + pathProvided + "\"");
    }

    /**
     * Verifies presence of a particular element in a JSONArray in the specified response
     * <p>
     * Uses JsonPath to search for the value and verifies if it is a JSONArray or not beforehand.
     * <p>
     * Does not work with Array of JSONObjects as they will have an extended JsonPath.
     *
     * @param pathProvided  JSON Path of the parent JSON Array.
     * @param expectedValue Value of item expected to be found in the JSON Array found using the provided JSON Path.
     * @param responseKey   Key name of the request associated with this response.
     */
    public void verifyValueInJsonArray(String pathProvided, String expectedValue, String responseKey) {
        if (!responseMap.containsKey(responseKey)) {
            logger.logError("No such response found. Please recheck the provided Response Key: " + responseKey);
            Assert.fail("No such response found. Please recheck the provided Response Key: " + responseKey);
        }
        Response res = responseMap.get(responseKey);
        Object valueFoundAtPath = res.jsonPath().get(pathProvided);

        if (ObjectUtils.allNull(valueFoundAtPath)) {
            logger.logError("No value found for the provided key: \"" + pathProvided + "\"");
            Assert.fail("No value found for the provided key: \"" + pathProvided + "\"");
        } else if (valueFoundAtPath instanceof ArrayList) {
            ArrayList arr = (ArrayList) valueFoundAtPath;

            if (!(arr.get(0) instanceof JSONObject)) {
                boolean found = false;
                for (Object obj : arr) {
                    found = String.valueOf(obj).equals(String.valueOf(expectedValue));
                    if (found) break;
                }

                if (!found) {
                    logger.logError("Couldn't find " + expectedValue + " in response for the provided key: \"" + pathProvided + "\"");
                    Assert.fail("Couldn't find " + expectedValue + " in response for the provided key: \"" + pathProvided + "\"");
                }
            } else {
                logger.logError("Value for the provided key: " + pathProvided + " is a JSONArray of JSONObjects. Can't validate the value");
                Assert.fail("Value for the provided key: " + pathProvided + " is a JSONArray of JSONObjects. Can't validate the value");
            }
        } else {
            logger.logError("Value for the provided key: " + pathProvided + " is not a JSONArray. Can't validate the value");
            Assert.fail("Value for the provided key: " + pathProvided + " is not a JSONArray. Can't validate the value");
        }

        if (pathProvided.split("\\.").length == 1)
            logger.logInfo("Successfully verified presence of " + expectedValue + " in response for the provided key: \"" + pathProvided + "\"");
        else
            logger.logInfo("Successfully verified presence of " + expectedValue + " in response for the provided path: \"" + pathProvided + "\"");

    }

    /**
     * Verifies whether the latest response matches the specified JSON Schema
     *
     * @param schemaName Name of the Schema which is to be referred to for comparison. Must be in the src\test\resources\schemas directory.
     */
    public void verifyResponseSchema(String schemaName) {
        Response res = responseMap.get(latestResponseKey);
        try {
            res.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\schemas\\" + schemaName + ".json")));
        } catch (Exception e) {
            if (e.getMessage().contains("JsonMappingException") || e.getMessage().contains("IOException")) {
                logger.logError("Failed to validate Schema. Body may not match expectations. Exception: " + e.getMessage());
                Assert.fail("Failed to validate Schema. Body may not match expectations. Exception: " + e.getMessage());
            } else if (e.getMessage().contains("FileNotFoundException")) {
                logger.logError("Failed to validate Schema. Could not find the reference Schema");
                Assert.fail("Failed to validate Schema. Could not find the reference Schema");
            } else if (e.getMessage().contains("JsonParseException")) {
                logger.logError("Failed to validate Schema. Reference Schema is invalid");
                Assert.fail("Failed to validate Schema. Reference Schema is invalid");
            } else {
                logger.logError("Failed to validate Schema due to Exception: " + e);
                Assert.fail("Failed to validate Schema due to Exception: " + e);
            }
        }
        logger.logInfo("Response Schema verified successfully.");
    }

    /**
     * Verifies whether the specified response matches the specified JSON Schema
     *
     * @param schemaName  Name of the Schema which is to be referred to for comparison. Must be in the src\test\resources\schemas directory.
     * @param responseKey Key name of the request associated with this response
     */
    public void verifyResponseSchema(String schemaName, String responseKey) {
        if (!responseMap.containsKey(responseKey)) {
            logger.logError("No such request found: " + responseKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + responseKey + ". Kindly re-check the Request Name.");
        }

        if (!responseMap.containsKey(responseKey)) {
            logger.logError("No such response found. Please recheck the provided Response Key: " + responseKey);
            Assert.fail("No such response found. Please recheck the provided Response Key: " + responseKey);
        } else {
            Response res = responseMap.get(responseKey);
            try {
                res.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\schemas\\" + schemaName + ".json")));
            } catch (Exception e) {
                if (e instanceof JsonMappingException || e instanceof FileNotFoundException || e instanceof IOException) {
                    logger.logError("Failed to validate Schema. Body may not match expectations. Exception: " + e);
                    Assert.fail("Failed to validate Schema. Body may not match expectations. Exception: " + e);
                } else {
                    logger.logError("Failed to validate Schema due to Exception: " + e);
                    Assert.fail("Failed to validate Schema due to Exception: " + e);
                }
            }
        }
        logger.logInfo("Response Schema verified successfully.");
    }


    /**
     * Toggles URL Encoding for the latest RequestSpecification
     *
     * @param encodingStatus Toggle to set encoding on or off.
     */
    public void setUrlEncoding(boolean encodingStatus) {
        reqSpecMap.get(latestResponseKey).urlEncodingEnabled(encodingStatus);
    }

    /**
     * Toggles URL Encoding for the specified RequestSpecification
     *
     * @param encodingStatus Toggle to set encoding on or off.
     * @param requestKey     Key name of the request associated with this response
     */
    public void setUrlEncoding(boolean encodingStatus, String requestKey) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }

        reqSpecMap.get(requestKey).urlEncodingEnabled(encodingStatus);
    }

    /**
     * Configures the latest RequestSpecification to use relaxed HTTP validation
     */
    public void setRelaxedHttpsValidation() {
        reqSpecMap.get(latestResponseKey).relaxedHTTPSValidation();
    }

    /**
     * Configures the specified RequestSpecification to use relaxed HTTP validation
     *
     * @param requestKey Key name of the request associated with this response
     */
    public void setRelaxedHttpsValidation(String requestKey) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }

        reqSpecMap.get(requestKey).relaxedHTTPSValidation();
    }

    /**
     * Adds a file to be uploaded. Also sets the contentType of the latest request to "multipart/form-data" automatically
     * <p>
     * MIME type is auto-set
     *
     * @param controlName Control Name of the file to be uploaded
     * @param filename    Name of the file to be uploaded. Must be in the src/test/resources/multipartfiles directory.
     */
    public void addMultipartFileToRequest(String controlName, String filename) {
        if (filename.split("\\.").length == 1) {
            logger.logError("No File extension detected. Please provide filename with extension");
            Assert.fail("No File extension detected. Please provide filename with extension");
        }
        File file = new File("src/test/resources/multipartfiles/" + filename);
        reqSpecMap.get(latestResponseKey).multiPart(controlName, file);
    }

    /**
     * Adds a file to be uploaded. Also sets the contentType of the specified request to "multipart/form-data" automatically
     * <p>
     * MIME type is auto-set
     *
     * @param controlName Control Name of the file to be uploaded
     * @param filename    Name of the file to be uploaded. Must be in the src/test/resources/multipartfiles directory.
     * @param requestKey  Key name of the request associated with this response
     */
    public void addMultipartFileToRequest(String controlName, String filename, String requestKey) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }

        if (filename.split("\\.").length == 1) {
            logger.logError("No File extension detected. Please provide filename with extension");
            Assert.fail("No File extension detected. Please provide filename with extension");
        }

        File file = new File("src/test/resources/multipartfiles/" + filename);
        reqSpecMap.get(requestKey).multiPart(controlName, file);
    }

    /**
     * Adds a file to be uploaded. Also sets the contentType of the latest request to "multipart/form-data" automatically
     * <p>Sets the MIME type according to the parameter passed
     *
     * @param controlName Control Name of the file to be uploaded
     * @param filename    Name of the file to be uploaded. Must be in the src/test/resources/multipartfiles directory.
     * @param mimeType    MIME type of the file to be uploaded
     */
    public void addMultipartFileToRequestWithMIMEType(String controlName, String filename, String mimeType) {
        if (filename.split("\\.").length == 1) {
            logger.logError("No File extension detected. Please provide filename with extension");
            Assert.fail("No File extension detected. Please provide filename with extension");
        }
        File file = new File("src/test/resources/multipartfiles/" + filename);
        reqSpecMap.get(latestResponseKey).multiPart(controlName, file, mimeType);
    }

    /**
     * Adds a file to be uploaded. Also sets the contentType of the active request to "multipart/form-data" automatically
     * <p>Sets the MIME type according to the parameter passed
     *
     * @param controlName Control Name of the file to be uploaded
     * @param filename    Name of the file to be uploaded. Must be in the src/test/resources/multipartfiles directory.
     * @param mimeType    MIME type of the file to be uploaded
     * @param requestKey  Key name of the request associated with this response
     */
    public void addMultipartFileToRequestWithMIMEType(String filename, String controlName, String mimeType, String requestKey) {
        if (!reqSpecMap.containsKey(requestKey)) {
            logger.logError("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
            Assert.fail("No such request found: " + requestKey + ". Kindly re-check the Request Name.");
        }

        if (filename.split("\\.").length == 1) {
            logger.logError("No File extension detected. Please provide filename with extension");
            Assert.fail("No File extension detected. Please provide filename with extension");
        }

        File file = new File("src/test/resources/multipartfiles/" + filename);
        reqSpecMap.get(requestKey).multiPart(controlName, file, mimeType);
    }

    /**
     * Reads the Json.Request directory for an environment specific Body file and extracts the desired body based on the passed key
     *
     * @param key         Key of the particular json body which will be passed as a request
     * @param environment Data is fetched from files based on the environment
     */
    public String jsonBody(String key, String environment) {
        String fileName = environment + "_data.json";
        try {
            String fullPath = System.getProperty("user.dir") + "//src//test//resources//requestbodies//" + fileName;
            String jsonContent = null;
            try {
                jsonContent = new String(Files.readAllBytes(Paths.get(fullPath)));
            } catch (Exception e) {
                logger.logInfo("Unknown environment: " + environment);
                Assert.fail("Unknown environment: " + environment);
            }

            //Deserialization and Serialization both actions are performed here

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonData = objectMapper.readValue(jsonContent, new TypeReference<>() {
            });

            if (jsonData.containsKey(key)) {
                Object selectedJsonObject = jsonData.get(key);
                return objectMapper.writeValueAsString(selectedJsonObject);
            } else {
                throw new IllegalArgumentException("JSON object with key '" + key + "' not found in the JSON file");
            }
        } catch (IOException e) {
            logger.logError("No such file found in directory: " + fileName);
            Assert.fail("No such file found in directory: " + fileName);
            return null;
        }
    }

    /**
     * Reads the Json.Request directory for an environment specific Body file and extracts the desired body based on the passed key
     * Then, adds values extracted from other responses to specified JsonPaths
     * <p>This function is used when the request body of one request depends on the response of another</p>
     *
     * @param key         Key of the particular json body which will be passed as a request
     * @param environment Data is fetched from files based on the environment
     * @param newKeyPath  The Json Path of the new key that is to be added
     * @param keyToAdd    The key whose value is fetched from another response and needs to be added in the body of new request
     */
    public String jsonBody(String key, String environment, String newKeyPath, String keyToAdd, boolean overwrite) {
        String fileName = environment.toLowerCase() + "_data.json";
        try {
            String fullPath = System.getProperty("user.dir") + "//src//test//resources//requestbodies//" + fileName;
            String jsonContent = new String(Files.readAllBytes(Paths.get(fullPath)));
            JSONObject parentObject = new JSONObject(jsonContent);
            JSONObject jsonObject = parentObject.getJSONObject(key);
            String[] keys = newKeyPath.split("\\.");
            JSONObject nestedObject = jsonObject;

            for (int i = 0; i < keys.length - 1; i++) {
                String pathPart = keys[i];
                if (pathPart.contains("[")) {
                    // Handle array indexing
                    String arrayName = pathPart.substring(0, pathPart.indexOf("["));
                    int index = Integer.parseInt(pathPart.replaceAll("[^0-9]", ""));

                    if (nestedObject.has(arrayName) && nestedObject.get(arrayName) instanceof JSONArray) {
                        JSONArray jsonArray = nestedObject.getJSONArray(arrayName);

                        if (index >= 0 && index < jsonArray.length()) {
                            nestedObject = jsonArray.getJSONObject(index);
                        } else if (index == jsonArray.length()) {
                            // If the index is one greater than the current length, create a new object
                            JSONObject newObject = new JSONObject();
                            jsonArray.put(newObject);
                            nestedObject = newObject;
                        } else {
                            // Handle invalid index
                            logger.logError("Invalid index in key path: " + newKeyPath);
                            Assert.fail("Invalid index in key path: " + newKeyPath);
                            return null;
                        }
                    } else {
                        // Handle array not found; create a new array and object
                        JSONArray newArray = new JSONArray();
                        JSONObject newObject = new JSONObject();
                        newArray.put(newObject);
                        nestedObject.put(arrayName, newArray);
                        nestedObject = newObject;
                    }
                } else {
                    // Handle object properties
                    if (nestedObject.has(pathPart)) {
                        nestedObject = nestedObject.getJSONObject(pathPart);
                    } else {
                        // Handle key not found; create a new object
                        JSONObject newObject = new JSONObject();
                        nestedObject.put(pathPart, newObject);
                        nestedObject = newObject;
                    }
                }
            }

            // Extract the key to update from the last part of newKeyPath
            String keyToUpdate = keys[keys.length - 1];

            // Update the value of the extracted key in the nested JSON object
            nestedObject.put(keyToUpdate, keyToAdd);

            // Update the JSON content in memory
            parentObject.put(key, jsonObject);

            // Write the updated JSON content back to the file if overwrite is true
            if (overwrite) {
                try (FileWriter fileWriter = new FileWriter(fullPath)) {
                    fileWriter.write(parentObject.toString(2));
                    fileWriter.flush(); // Ensure the data is written immediately
                }
            }

            return parentObject.getJSONObject(key).toString(2);
        } catch (IOException e) {
            logger.logError("No such file found in directory: " + fileName);
            Assert.fail("No such file found in directory: " + fileName);
            return null;
        }
    }

    /**
     * Adds basic authentication to the latest request
     * Fetches credentials from serenity.conf file
     */
    public void addBasicAuth() {
        String username = EnvironmentSpecificConfiguration.from(variables).getProperty("username");
        String password = new String(Base64.decode(Unpooled.wrappedBuffer(EnvironmentSpecificConfiguration.from(variables).getProperty("password").getBytes())).array());
        reqSpecMap.get(latestResponseKey).auth().basic(username, password);
    }

    /**
     * Adds basic authentication to the specified request
     * Fetches credentials from serenity.conf file
     */
    public void addBasicAuth(String requestKey) {
        String username = EnvironmentSpecificConfiguration.from(variables).getProperty("username");
        String password = new String(Base64.decode(Unpooled.wrappedBuffer(EnvironmentSpecificConfiguration.from(variables).getProperty("password").getBytes())).array());
        reqSpecMap.get(requestKey).auth().basic(username, password);
    }

    /**
     * Adds bearer authentication to the latest request
     * Fetches Bearer token from serenity.conf file
     */
    public void addBearerAuth() {
        String token = UtilStepDefinition.generateBearerToken();
        // The above function "generateAccessToken()" can be modified to generate a Bearer token.
        reqSpecMap.get(latestResponseKey).headers("Authorization", "Bearer " + token);
    }

    /**
     * Adds bearer authentication to the latest request
     * Fetches Bearer token from serenity.conf file
     */
    public void addBearerAuth(String requestKey) {
        String token = UtilStepDefinition.generateBearerToken();
        // The above function "generateAccessToken()" can be modified to generate a Bearer token.
        reqSpecMap.get(requestKey).headers("Authorization", "Bearer " + token);
    }

    /**
     * Clears the Request Specification Hashmap
     */
    public void clearRequests() {
        reqSpecMap = new HashMap<>();
    }

    /**
     * Clears the Response Hashmap
     */
    public void clearResponses() {
        responseMap = new HashMap<>();
    }

    /**
     * Removes a specific request from memory
     *
     * @param key Request Key that needs to be removed from memory
     */
    public void clearRequest(String key) {
        reqSpecMap.remove(key);
    }

    /**
     * Removes a specific response from memory
     *
     * @param key Response Key that needs to be removed from memory
     */
    public void clearResponse(String key) {
        responseMap.remove(key);
    }

    //------------------------------------------ Comparator Code -----------------------------------------------

    /**
     * Json Comparison is performed based on the type of Json
     */

    public void performComparison(String response1, String responseKey2) throws IOException {
        if (typeJSON(response1, responseKey2).equalsIgnoreCase("object")) {
            JSONObject expected = getExpectedJsonObject();
            JSONObject observed = getResultJsonObject();
            //compares two JSON objects and returns the HashSet containing all the differences
            HashSet<String> jsonDiff = compareJsonObject(expected, observed);
            //passes the HashSet containing differences to update the automation report.
            updateReport(jsonDiff);
        } else if (typeJSON(response1, responseKey2).equalsIgnoreCase("array")) {
            JSONArray expected = getExpectedJSONArray();
            JSONArray observed = getResultJSONArray();
            //compares two JSON Arrays and returns the HashSet containing all the differences
            HashSet<String> jsonDiff = compareJsonArray(expected, observed);
            updateReport(jsonDiff);
        }
    }

    /**
     * Functions to determine the type of JSONs, read them from file and return them as JSON Objects
     * or JSON Arrays for further comparison between them.
     *
     * @return type Of Json
     */
    public String typeJSON(String response1, String responseKey2) {
        String type = null;
        expectedJson = responseMap.get(response1).asPrettyString();
        res = res + responseMap.get(responseKey2).asPrettyString();

        Object expJson = new JSONTokener(expectedJson).nextValue();
        Object respJson = new JSONTokener(res).nextValue();

        if (expJson instanceof JSONObject && respJson instanceof JSONObject) {
            type = "object";
        } else if (expJson instanceof JSONArray && respJson instanceof JSONArray) {
            type = "array";
        } else {
            Assert.fail("Both JSONs are not of same type");
        }

        return type;
    }

    /**
     * Function to read expected JSON Object from the file
     *
     * @return JsonObject
     */

    public JSONObject getExpectedJsonObject() {

        //converting expected json string to a json object
        JSONObject obj = new JSONObject(expectedJson);
        return obj;
    }

    /**
     * Function to read observed JSON Object from the file
     *
     * @return Json Object
     */

    public JSONObject getResultJsonObject() {
        //response string to json object
        JSONObject obj2 = new JSONObject(res);
        return obj2;
    }

    /**
     * Function to read expected JSON Array from the file
     *
     * @return :Json Array
     */

    public JSONArray getExpectedJSONArray() {
        JSONArray obj = new JSONArray(expectedJson);
        return obj;
    }

    /**
     * Function to read observed JSON Array from the file
     *
     * @return :Json Array
     */

    public JSONArray getResultJSONArray() {
        JSONArray obj = new JSONArray(res);
        return obj;
    }

    /**
     * Function for comparing JSON Objects
     *
     * @param expected Expected Json Object
     * @param result   Actual Json Object
     * @return HashSet of Values after Comparison
     */

    public HashSet<String> compareJsonObject(JSONObject expected, JSONObject result) {
        mismatchComments = new HashSet<>();
        mismatchedFinal = new HashMap<>();
        mismatchInJson1Keys = new ArrayList<>();
        mismatchInJson2Keys = new ArrayList<>();
        mismatchInJson1Values = new HashSet<>();
        mismatchInJson2Values = new HashSet<>();
        //converting expected json and result json objects to map
        Map<String, Object> expectedMap = toMap(expected);
        Map<String, Object> resultMap = toMap(result);


        //first step is to match the Keys
        matchKeys(resultMap, expectedMap);

        //match values
        matchValues(resultMap, expectedMap);

        //returns the HashSet containing all the differences.
        return returnResult();

    }

    /**
     * Function for comparing JSON Array
     *
     * @param expected Expected Json Array
     * @param result   Actual Json Array
     * @return HashSet of Values after Comparison
     */
    public HashSet<String> compareJsonArray(JSONArray expected, JSONArray result) {
        mismatchComments = new HashSet<>();
        mismatchedFinal = new HashMap<>();
        mismatchInJson1Keys = new ArrayList<>();
        mismatchInJson2Keys = new ArrayList<>();
        mismatchInJson1Values = new HashSet<>();
        mismatchInJson2Values = new HashSet<>();
        List<Object> expArray = toList(expected);
        List<Object> resArray = toList(result);
        compareArrayList((ArrayList<Object>) expArray, (ArrayList<Object>) resArray);

        //returns the HashSet containing all the differences.
        return returnResult();
    }

    /**
     * Function to update the serenity report with the differences (if any) passed as parameter.
     *
     * @param result Hashset of result of Json Differences
     */

    public void updateReport(HashSet<String> result) {
        if (result.size() != 0) {
            System.out.println("Differences found in Expected JSON and Observed JSON !!");
            Serenity.recordReportData().withTitle("Differences found in JSON").andContents((result.toString().substring(1, result.toString().length() - 1).replaceAll(",", ",\n")));
        } else {
            Serenity.recordReportData().withTitle("No Differences found in JSON").andContents("No Data Found");
            Assert.assertTrue("No differences found", true);
        }
    }

    /**
     * Function to get type of value for a better mismatch
     *
     * @param data Type of Data
     */
    public String getType(Object data) {
        String type = data.getClass().toString();
        if (type.contains("HashMap"))
            return "Map";
        else if (type.contains("ArrayList"))
            return "List";
        else if (type.contains("String"))
            return "String";
        else if (type.contains("Integer"))
            return "Integer";
        else if (type.contains("Boolean"))
            return "Boolean";
        else if (type.contains("Long"))
            return "long";
        else
            return "invalid";
    }

    /**
     * Function for compiling the differences into a single HashSet and returning it
     */
    public HashSet<String> returnResult() {
        finalResult = new HashSet<>();

        finalResult.addAll(mismatchComments);
        if (mismatchedFinal.size() > 0) {
            if (mismatchedFinal.containsKey("missingKeysInExpectedJSON")) {
                Object missingExp = mismatchedFinal.get("missingKeysInExpectedJSON");
                for (String st : (ArrayList<String>) missingExp)
                    finalResult.add("<" + st + "> key is missing in Expected JSON");
            }
            if (mismatchedFinal.containsKey("missingKeysInObservedJSON")) {
                Object missingExp = mismatchedFinal.get("missingKeysInObservedJSON");
                for (String st : (ArrayList<String>) missingExp)
                    finalResult.add("<" + st + "> key is missing in Observed JSON");
            }
        }
        return finalResult;
    }

    /**
     * Function to compare the ArrayLists
     *
     * @param listToCompare  List which needs to be compared
     * @param comparisonList List from which other list are compared
     */

    public void compareArrayList(ArrayList<Object> comparisonList, ArrayList<Object> listToCompare) {
        int i = 0;
        int j = 0;
        if (comparisonList.size() == listToCompare.size()) {
            for (Object listObj : listToCompare) {
                if (listObj instanceof HashMap) {
                    if (comparisonList.get(i) instanceof HashMap) {
                        matchKeys((Map<String, Object>) listObj, (Map<String, Object>) comparisonList.get(i));
                        matchValues((Map<String, Object>) listObj, (Map<String, Object>) comparisonList.get(i));
                    } else {
                        String comm = "Type Mismatch for <" + i + "> index in the list. Expected: <" + getType(comparisonList.get(i)) + "> type value | Observed: <" + getType(listObj) + "> type value";
                        mismatchComments.add(comm);
                    }
                } else if (listObj instanceof ArrayList) {
                    if (comparisonList.get(i) instanceof ArrayList) {

                        compareArrayList((ArrayList<Object>) comparisonList.get(i), (ArrayList<Object>) listObj);

                    } else {
                        String comm = "Type Mismatch for <" + i + "> index in the list. Expected: <" + getType(comparisonList.get(i)) + "> type value | Observed: <" + getType(listObj) + "> type value";
                        mismatchComments.add(comm);
                    }
                }
                //normal Arraylist values
                else {
                    //Arraylist values mismatch
                    if (!listObj.equals(comparisonList.get(i))) {
                        String listObjType = getType(listObj);
                        String compListType = getType(comparisonList.get(i));

                        //if the type is same then values differ
                        String comm;
                        if (listObjType.equalsIgnoreCase(compListType)) {
                            comm = "Value Mismatch for <" + i + "> index in the list. Expected: <" + comparisonList.get(i) + "> | Observed: <" + listObj + ">";
                        } else {
                            comm = "Type Mismatch for <" + i + "> index in the list. Expected: <" + compListType + "> type value | Observed: <" + listObjType + "> type value";
                        }
                        mismatchComments.add(comm);
                    }
                    //concatenate the mismatches in result
                    if (!mismatchInJson1Values.isEmpty() && !mismatchInJson2Values.isEmpty()) {
                        mismatchedFinal.put("missingValuesInExpectedJSON", mismatchInJson1Values);
                        mismatchedFinal.put("missingValuesInObservedJSON", mismatchInJson2Values);
                    }
                }

                i++;
            }
        } else {
            while (i < listToCompare.size() && j < comparisonList.size()) {
                if (!comparisonList.get(i).equals(listToCompare.get(i))) {
                    String listObjType = getType(listToCompare.get(i));
                    String compListType = getType(comparisonList.get(i));
                    String comm;
                    if (listObjType.equalsIgnoreCase(compListType)) {
                        comm = "Value Mismatch for <" + i + "> index in the list. Expected: <" + comparisonList.get(i) + "> | Observed: <" + listToCompare.get(i) + ">";
                    } else {
                        comm = "Type Mismatch for <" + i + "> index in the list. Expected: <" + compListType + "> type value | Observed: <" + listObjType + "> type value";
                    }
                    mismatchComments.add(comm);
                }
                i++;
                j++;
            }
            while (i < comparisonList.size()) {
                String comm = "Missing element <" + comparisonList.get(i) + "> at <" + i + "> index of list in observed JSON!";
                mismatchComments.add(comm);
                i++;
            }
            while (j < listToCompare.size()) {
                String comm = "Missing element <" + listToCompare.get(i) + "> at <" + i + "> index of list in expected JSON!";
                mismatchComments.add(comm);
                j++;
            }
        }
    }

    /**
     * Function to get value of key from Result Hashmap and expected hashmap and then compare it
     *
     * @param currKey     Key whose value needs to be compared
     * @param expectedMap Hashmap of expected Json
     * @param resultMap   Hashmap of result json
     */

    public void compareValue(String currKey, Map<String, Object> resultMap, Map<String, Object> expectedMap) {
        //get the values for current key from both maps
        Object resValue = resultMap.get(currKey);
        Object exValue = expectedMap.get(currKey);

        //if one of the value is of type Hashmap
        if (resValue instanceof HashMap || exValue instanceof HashMap) {
            //check if both are same type, go ahead and consider those hashmaps as new maps and compare from start
            if (exValue instanceof HashMap && resValue instanceof HashMap) {
                matchKeys((Map<String, Object>) resValue, (Map<String, Object>) exValue);
                matchValues((Map<String, Object>) resValue, (Map<String, Object>) exValue);
            } else {
                String comm = "Type Mismatch for key <" + currKey + ">. Expected: <" + getType(exValue) + "> type value | Observed: <" + getType(resValue) + "> type value";
                mismatchComments.add(comm);
            }
        }

        //if one of them is arraylist type, check if both are same else declare not of same type
        else if (resValue instanceof ArrayList || exValue instanceof ArrayList) {
            //if both are arraylist, go ahead and compare the sizes and then the values of arraylist
            if (resValue instanceof ArrayList && exValue instanceof ArrayList) {
                compareArrayList((ArrayList<Object>) exValue, (ArrayList<Object>) resValue);

            } else {
                String comm = "Type Mismatch for key <" + currKey + ">. Expected: <" + getType(exValue) + "> type value | Observed: <" + getType(resValue) + "> type value";
                mismatchComments.add(comm);
            }
        }

        //if the values are not map or list
        else {
            //case2 => when value for a key are not same
            if (!resValue.equals(exValue)) {
                String expectedType = getType(exValue);
                String resultType = getType(resValue);
                //2a => if the value 'type' is same but value are diff
                String comm;
                if (expectedType.equalsIgnoreCase(resultType)) {
                    comm = "Value Mismatch for key <" + currKey + ">. Expected: <" + exValue + "> | Observed: <" + resValue + ">";
                } else {
                    comm = "Type Mismatch for key <" + currKey + ">. Expected: <" + expectedType + "> type value | Observed: <" + resultType + "> type value";
                }
                mismatchComments.add(comm);
            }
            if (!mismatchInJson1Values.isEmpty() && !mismatchInJson2Values.isEmpty()) {
                mismatchedFinal.put("missingValuesInExpectedJSON", mismatchInJson1Values);
                mismatchedFinal.put("missingValuesInObservedJSON", mismatchInJson2Values);
            }
        }

    }

    /**
     * Function to compare keys of actual and expected Hashmap of json data and return Array list of Values
     *
     * @param comparisonMap Map from which actual map will be compared
     * @param mapToCompare  Actual map that needs to be compared
     */

    public ArrayList<String> compareKeys(Map<String, Object> comparisonMap, Map<String, Object> mapToCompare) {
        Iterator<String> mapKeys = mapToCompare.keySet().iterator();
        ArrayList<String> temp = new ArrayList<>();
        boolean flag = false;
        while (mapKeys.hasNext()) {
            String currKey = mapKeys.next();
            if (!comparisonMap.containsKey(currKey)) {
                flag = true;
                temp.add(currKey);
            }
        }
        if (flag)
            return temp;
        return null;
    }


    /**
     * Function to match keys of actual result map and expected map
     *
     * @param resultMap   Map from which keys will be matched with expected Map
     * @param expectedMap Expected Map
     */

    public void matchKeys(Map<String, Object> resultMap, Map<String, Object> expectedMap) {
        //if the keySet in both maps are unequal go ahead to match their keys further
        if (!resultMap.keySet().equals(expectedMap.keySet())) {
            //gets the keys that are missing in result json and adds to final hashmap
            ArrayList<String> missingKeysInResult = compareKeys(resultMap, expectedMap);
            if (missingKeysInResult != null) {
                missingKeysInResult.removeAll(mismatchInJson2Keys);
                mismatchInJson2Keys.addAll(missingKeysInResult);
                mismatchedFinal.put("missingKeysInObservedJSON", mismatchInJson2Keys);
            }

            //gets the keys that are missing in expected json and adds to final hashmap
            ArrayList<String> missingKeysInExpected = compareKeys(expectedMap, resultMap);
            if (missingKeysInExpected != null) {
                missingKeysInExpected.removeAll(mismatchInJson1Keys);
                mismatchInJson1Keys.addAll(missingKeysInExpected);
                mismatchedFinal.put("missingKeysInExpectedJSON", mismatchInJson1Keys);
            }
        } else {
            matchValues(resultMap, expectedMap);
        }

    }

    /**
     * Function to match values of actual result map and expected map
     *
     * @param resultMap   - Actual map to be compared
     * @param expectedMap – Expected map
     */

    public void matchValues(Map<String, Object> resultMap, Map<String, Object> expectedMap) {
        Iterator<String> expectedMapKeys = expectedMap.keySet().iterator();
        Iterator<String> resultMapKeys = resultMap.keySet().iterator();

        //get the keys that are mismatched in result and expected so that they can be omitted while comparing the individual keys' values
        Object resKey = mismatchedFinal.get("missingKeysInObservedJSON");
        Object exKey = mismatchedFinal.get("missingKeysInExpectedJSON");

        matchJsonValues(resultMap, expectedMap, expectedMapKeys, resKey);
        matchJsonValues(resultMap, expectedMap, resultMapKeys, exKey);
    }

    /**
     * Function to match json Values
     */

    public void matchJsonValues(Map<String, Object> resultMap, Map<String, Object> expectedMap, Iterator<String> it, Object mismatchedKeys) {
        List<String> keysNotToMatch;

        //if there are some keys to be omitted
        if (mismatchedKeys != null) {
            keysNotToMatch = ((ArrayList<String>) mismatchedKeys);
            //check if the current key is not to be omitted , go ahead and compare their values
            while (it.hasNext()) {
                String currKey = it.next();
                if (!keysNotToMatch.contains(currKey)) {
                    compareValue(currKey, resultMap, expectedMap);
                }
            }
        }

        //if there are no keys to be omitted then compare all the keys of both json
        else {
            while (it.hasNext()) {
                String currKey = it.next();
                compareValue(currKey, resultMap, expectedMap);
            }
        }
    }

    /**
     * Function to convert Json Object to Map
     *
     * @param jsonObj JsonObject to be converted into Map
     */
    public Map<String, Object> toMap(JSONObject jsonObj) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        //iterating through the keys of passed json object
        Iterator<String> keys = jsonObj.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            //for each key getting the value object
            Object value = jsonObj.get(key);
            //if value is of jsonArray type convert to List
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
                //if value is again a jsonObject convert it into map
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            //if value is a simple key,value pair type put in the map
            map.put(key, value);
        }
        return map;
    }

    /**
     * Function to convert Json Array to List
     *
     * @param jsonArray JsonArray to be converted into list
     */
    public List<Object> toList(JSONArray jsonArray) throws JSONException {
        List<Object> list = new ArrayList<>();
        //for each value in the list
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            //if it's an array convert to list
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            //if it's an object convert to map
            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            //else add to the list
            list.add(value);
        }
        return list;

    }


}