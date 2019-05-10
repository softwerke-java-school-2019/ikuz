package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

/**
 * Validate value entered by user for a query key that may require multiple string arguments separated by commas
 */
public class MultiStringParam extends StringParam {
    private final List<String> valueList;
    
    /**
     * Validate value entered by user. Converts all of the characters in this {@code String} to lower case and
     * make strings with any leading and trailing whitespace removed. Stores the list of created strings.
     *
     * @param value string entered by user - the value of filter or order parameter key in query
     * @param fieldName auxiliary parameter needed to display the correct error message - the key of filter or order query
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST}
     *         if the entered value is empty string on null or has too large length (if it thrown by {@link StringParam} superclass)
     *         if at least one of the list items separated by commas is empty or null
     */
    public MultiStringParam(String value, String fieldName) throws WebApplicationException {
        super(value, fieldName);
        List<String> valueList = Arrays.asList(StringUtils.splitPreserveAllTokens(value, ","));
    
        for (int i = 0; i < valueList.size(); ++i) {
            if (StringUtils.isBlank(valueList.get(i))) {
                Response response = QueryUtils.getResponseWithMessage(
                        Response.Status.BAD_REQUEST,
                        QueryUtils.EMPTY_OR_NULL_VALUE_TYPE_ERROR,
                        QueryUtils.getEmptyOrNullValueMessage(value, fieldName)
                );
                throw new WebApplicationException(response);
            }
            valueList.set(i, StringUtils.trim(valueList.get(i)));
        }
        this.valueList = valueList;
    }
    
    public List<String> getValueList() {
        return valueList;
    }
}
