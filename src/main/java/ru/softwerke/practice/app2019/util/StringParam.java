package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Validate value entered by user for a query key that requires only one string argument
 */
public class StringParam {
    private final String value;
    
    /**
     * Validate value entered by user. Converts all of the characters in this {@code String} to lower case and
     * make string with any leading and trailing whitespace removed, parses a string into a list of several values for a given query key.
     * Stores the created string.
     *
     * @param value string entered by user - the value of filter or order parameter key in query
     * @param fieldName auxiliary parameter needed to display the correct error message - the key of filter or order query
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST}
     *         if the entered value is empty string on null or has too large length
     */
    public StringParam(String value, String fieldName) throws WebApplicationException {
        QueryUtils.checkLengthOfHeaderFields(value, fieldName);
        if (StringUtils.isBlank(value)) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.EMPTY_OR_NULL_VALUE_TYPE_ERROR,
                    QueryUtils.getEmptyOrNullParamsMessage(fieldName)
            );
            throw new WebApplicationException(response);
        }
        this.value = value.toLowerCase().trim();
    }
    
    public String getValue() {
        return value;
    }
}
