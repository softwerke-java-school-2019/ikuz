package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;
import ru.softwerke.practice.app2019.handler.JSONErrorMessage;
import ru.softwerke.practice.app2019.model.BillItem;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Query assertions utility class
 */
public class QueryUtils {
    private static final int MAX_QUERY_FIELD_LENGTH = 50;
    private static final int MAX_COUNT = 1000;
    private static final String LARGE_NUMBER_ERROR = "too large number given";
    private static final String LARGE_FIELD_TYPE_ERROR = "too large length of field query";
    
    static final String EMPTY_OR_NULL_VALUE_TYPE_ERROR = "query parameter's value is empty or null";
    
    public static final String NOT_FOUND_VALUE_ERROR = "not found value of parameter in the database";
    public static final String INVALID_FORMAT_TYPE_ERROR = "invalid query parameter format";
    public static final String WRONG_PROPERTY_TYPE_ERROR = "wrong property";
    public static final String PARSING_TYPE_ERROR = "parsing error";
    public static final String INVALID_JSON_ERROR = "invalid json";
    public static final String MALFORMED_PARAMS_TYPE_ERROR = "malformed query parameters";
    public static final String EMPTY_REQUEST_TYPE_ERROR = "empty request";
    public static final String NOT_FOUND_TYPE_ERROR = "not found";
    
    private static String getNegativeNumberErrorMessage(String valueStr, String fieldName) {
        return String.format("the value '%s' of '%s' parameter is negative", valueStr, fieldName);
    }
    
    private static String getMalformedKeyParamsMessage(String key) {
        return String.format("query parameter '%s' is malformed for this entity", key);
    }
    
    /**
     * Assert that received value of corresponding key parameter does not have too large length
     *
     * @param stringValue value of key parameter of query
     * @param fieldName   key parameter of the query
     * @throws WebApplicationException with response's status 431 {@code Response.Status.REQUEST_HEADER_FIELDS_TOO_LARGE}
     *         if value has too large length
     */
    static void checkLengthOfHeaderFields(String stringValue, String fieldName) throws WebApplicationException {
        if (StringUtils.length(stringValue) > MAX_QUERY_FIELD_LENGTH) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.REQUEST_HEADER_FIELDS_TOO_LARGE,
                    QueryUtils.LARGE_FIELD_TYPE_ERROR,
                    String.format(
                            "the value '%s' of '%s' parameter has too large length",
                            stringValue,
                            fieldName
                    )
            );
            throw new WebApplicationException(response);
        }
    }
    
    static String getEmptyOrNullParamsMessage(String fieldName) {
        return String.format("'%s' query parameter's value is empty or null", fieldName);
    }
    
    static String getEmptyOrNullValueMessage(String value, String fieldName) {
        return String.format("in value '%s' of query parameter '%s' missed argument or it is null", value, fieldName);
    }
    
    static String getInvalidFormatMessage(String valueStr, String fieldName, String format) {
        return String.format("the value '%s' of '%s' parameter does not match %s",
                valueStr, fieldName, format);
    }
    
    /**
     * Assert that received value of number corresponds the positive number format
     *
     * @param stringValue value by {@code String} of number parameter of query
     * @param fieldName   key parameter of the query
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST} if value does not
     *         correspond the positive number format
     */
    static void checkIsPositiveNumber(String stringValue, String fieldName) throws WebApplicationException {
        if (stringValue.startsWith("-")) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                    QueryUtils.getNegativeNumberErrorMessage(stringValue, fieldName)
            );
            throw new WebApplicationException(response);
        }
    }
    
    /**
     * Assert that received value of date does not exceed today's date
     *
     * @param localDate value by {@code LocalDate} of date parameter of query
     * @param fieldName key parameter of the query
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST} if the date exceeds today's date
     */
    static void checkIsDateBeforeNow(LocalDate localDate, String fieldName) throws WebApplicationException {
        if (localDate.compareTo(LocalDate.now()) > 0) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                    String.format("the date of parameter '%s' can not exceed today's date", fieldName)
            );
            throw new WebApplicationException(response);
        }
    }
    
    /**
     * Assert that received value of date does not exceed current time
     *
     * @param localDateTime value by {@code LocalDateTime} of dateTime parameter of query
     * @param fieldName     key parameter of the query
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST} if the time exceeds current dateTime
     */
    static void checkIsDateTimeBeforeNow(LocalDateTime localDateTime, String fieldName) throws WebApplicationException {
        if (localDateTime.compareTo(LocalDateTime.now()) > 0) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                    String.format("the dateTime of parameter '%s' can not exceed current dateTime", fieldName)
            );
            throw new WebApplicationException(response);
        }
    }
    
    /**
     * Build response that may be taken by {@code WebApplicationException} with corresponding {@link JSONErrorMessage}
     *
     * @param status      response status
     * @param type        type of error
     * @param description description of error
     * @return response to client
     */
    public static Response getResponseWithMessage(Response.Status status, String type, String description) {
        return Response
                .status(status)
                .entity(JSONErrorMessage.create(status, type, description))
                .build();
    }
    
    public static String getMalformedValueParamsMessage(String type) {
        return String.format("the value '%s' of query parameter 'orderBy' is malformed for this entity", type);
    }
    
    public static String getWrongPropertyMessage(String propertyName) {
        return String.format("Unrecognized property '%s'", propertyName);
    }
    
    /**
     * Assert that received value of quantity objects per page is not too large
     *
     * @param count quantity of objects per page
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST}
     *         if quantity objects per page is too large
     */
    public static void checkCountValue(long count) throws WebApplicationException {
        if (count > MAX_COUNT) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.LARGE_NUMBER_ERROR,
                    "too many items on one page (expected less than 1000)"
            );
            throw new WebApplicationException(response);
        }
    }
    
    /**
     * Assert that received list of items is not empty or null and does not contain nulls
     *
     * @param items accepted list
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST}
     *         if list of items is null or empty or it contains nulls
     */
    public static void checkListForNulls(List<BillItem> items) throws WebApplicationException {
        if (items == null || items.isEmpty()) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.EMPTY_OR_NULL_VALUE_TYPE_ERROR,
                    getEmptyOrNullParamsMessage("items")
            );
            throw new WebApplicationException(response);
        }
        for (BillItem item : items) {
            if (item == null) {
                Response response = QueryUtils.getResponseWithMessage(
                        Response.Status.BAD_REQUEST,
                        QueryUtils.EMPTY_OR_NULL_VALUE_TYPE_ERROR,
                        "one of bill's items is null"
                );
                throw new WebApplicationException(response);
            }
        }
    }
    
    /**
     * Throws {@code WebApplicationException}, cause passed key parameter query is malformed
     *
     * @param key wrong key of query
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST},
     *         cause passed key parameter query is malformed
     */
    public static void getWrongParamsMessage(String key) throws WebApplicationException {
        Response response = QueryUtils.getResponseWithMessage(
                Response.Status.BAD_REQUEST,
                QueryUtils.MALFORMED_PARAMS_TYPE_ERROR,
                QueryUtils.getMalformedKeyParamsMessage(key)
        );
        throw new WebApplicationException(response);
    }
}
