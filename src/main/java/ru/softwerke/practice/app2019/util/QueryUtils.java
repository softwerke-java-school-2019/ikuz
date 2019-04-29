package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;
import ru.softwerke.practice.app2019.handler.JSONErrorMessage;
import ru.softwerke.practice.app2019.model.BillItem;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

public class QueryUtils {
    private static final int MAX_URI_LENGTH = 500;
    private static final int MAX_QUERY_FIELD_LENGTH = 50;
    private static final int MAX_COUNT = 1000;
    private static final String LARGE_URI_TYPE_ERROR = "too large length of URI";
    private static final String LARGE_NUMBER_ERROR = "еoo large number given";
    private static final String LARGE_FIELD_TYPE_ERROR = "too large length of field query";
    private static final String NOT_FOUND_VALUE_ERROR = "not found value of parameter in the database";
    static final String EMPTY_VALUE_TYPE_ERROR = "missed query parameter or it has empty or null value";
    static final String INVALID_FORMAT_TYPE_ERROR = "invalid query parameter format";
    public static final String WRONG_PROPERTY_TYPE_ERROR = "wrong property";
    public static final String PARSING_TYPE_ERROR = "parsing error";
    public static final String INVALID_JSON_ERROR = "invalid json";
    public static final String MALFORMED_PARAMS_TYPE_ERROR = "malformed query parameters";
    public static final String EMPTY_REQUEST_TYPE_ERROR = "empty request";
    public static final String NOT_FOUND_TYPE_ERROR = "not found";
    
    static void checkLengthOfHeaderFields(String value, String param, String typeQuery) {
        if (StringUtils.length(value) > MAX_QUERY_FIELD_LENGTH) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.REQUEST_HEADER_FIELDS_TOO_LARGE,
                            QueryUtils.LARGE_FIELD_TYPE_ERROR,
                            String.format("the value '%s' of '%s' parameter in '%s' query has too large length",
                                    value, param, typeQuery));
            throw new WebApplicationException(response);
        }
    }
    
    static String getEmptyOrNullParamsMessage(String typeField, String typeQuery) {
        return String.format("missed query parameter '%s' or it has empty or null value in '%s' query", typeField, typeQuery);
    }
    
    static String getNegativeNumberErrorMessage(String valueStr, String paramField, String typeQuery) {
        return String.format("the value '%s' of '%s' parameter in '%s' query is negative", valueStr, paramField, typeQuery);
    }
    
    static String getInvalidFormatMessage(String valueStr, String paramField, String typeQuery, String format) {
        return String.format("the value '%s' of '%s' parameter in '%s' query does not match %s",
                valueStr, paramField, typeQuery, format);
    }
    
    public static Response getResponseWithMessage(Response.Status status, String type, String description) {
        return Response
                .status(status)
                .entity(JSONErrorMessage.create(status, type, description))
                .build();
    }
    
    public static String getMalformedKeyParamsMessage(String entity, String key) {
        return String.format("%s query parameter '%s' is malformed", entity, key);
    }
    
    public static String getMalformedValueParamsMessage(String type, String entity) {
        return String.format("the value '%s' of %s query parameter 'orderBy' is malformed", type, entity);
    }
    
    public static String getWrongPropertyMessage(String propertyName) {
        return String.format("Unrecognized property '%s'", propertyName);
    }
    
    public static void checkCountValue(int count) throws WebApplicationException {
        if (count > MAX_COUNT) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.LARGE_NUMBER_ERROR,
                            "too many items on one page (expected less than 1000)");
            throw new WebApplicationException(response);
        }
    }
    
    public static void checkLengthQuery(int length) throws WebApplicationException {
        if (length > MAX_URI_LENGTH) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.REQUEST_URI_TOO_LONG,
                            QueryUtils.LARGE_URI_TYPE_ERROR,
                            "given URI has too large length");
            throw new WebApplicationException(response);
        }
    }
    
    public static void checkListForNulls(List<BillItem> items, String typeQuery) throws WebApplicationException {
        if (items == null || items.isEmpty()) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.EMPTY_VALUE_TYPE_ERROR,
                            getEmptyOrNullParamsMessage("items", typeQuery));
            throw new WebApplicationException(response);
        }
        for (BillItem item : items) {
            if (item == null) {
                Response response = QueryUtils.
                        getResponseWithMessage(Response.Status.BAD_REQUEST,
                                QueryUtils.EMPTY_VALUE_TYPE_ERROR,
                                String.format("one of items in '%s' query is null", typeQuery));
                throw new WebApplicationException(response);
            }
        }
    }
    
    public static int checkDoesColorExist(String colorName, String typeQuery) throws WebApplicationException {
        Map<String, Integer> colors = ColorTable.getColors();
        if (colors.containsKey(colorName)) {
            return colors.get(colorName);
        } else {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.NOT_FOUND_VALUE_ERROR,
                            String.format("сolor name '%s' not found in the table of available colors when requesting to '%s'", colorName, typeQuery));
            throw new WebApplicationException(response);
        }
    }
}
