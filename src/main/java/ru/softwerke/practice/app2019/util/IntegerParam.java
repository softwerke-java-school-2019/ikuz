package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class IntegerParam {
    private static final String FORMAT = "the integer number format";
    private final int intValue;
    private final String typeField;
    private final String typeQuery;
    
    public IntegerParam(String intValueStr, String typeField, String typeQuery) throws WebApplicationException {
        QueryUtils.checkLengthOfHeaderFields(intValueStr, typeField, typeQuery);
        if (StringUtils.isBlank(intValueStr)) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.EMPTY_VALUE_TYPE_ERROR,
                            QueryUtils.getEmptyOrNullParamsMessage(typeField, typeQuery));
            throw new WebApplicationException(response);
        }
        try {
            int temp = Integer.valueOf(intValueStr);
            if (temp >= 0) {
                this.intValue = temp;
                this.typeField = typeField;
                this.typeQuery = typeQuery;
            } else {
                Response response = QueryUtils.
                        getResponseWithMessage(Response.Status.BAD_REQUEST,
                                QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                                QueryUtils.getNegativeNumberErrorMessage(intValueStr, typeField, typeQuery));
                throw new WebApplicationException(response);
            }
        } catch (NumberFormatException e) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                            QueryUtils.getInvalidFormatMessage(intValueStr, typeField, typeQuery, FORMAT));
            throw new WebApplicationException(response);
        }
        
    }
    
    public int getIntegerValue() {
        return intValue;
    }
    
    public String getTypeField() {
        return typeField;
    }
    
    public String getTypeQuery() {
        return typeQuery;
    }
}
