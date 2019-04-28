package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class StringParam {
    private final String value;
    private final String typeField;
    private final String typeQuery;
    
    public StringParam(String value, String typeField, String typeQuery) throws WebApplicationException {
        QueryUtils.checkLengthOfHeaderFields(value, typeField, typeQuery);
        if (StringUtils.isNotBlank(value)) {
            this.value = value;
            this.typeField = typeField;
            this.typeQuery = typeQuery;
        } else {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.EMPTY_VALUE_TYPE_ERROR,
                            QueryUtils.getEmptyOrNullParamsMessage(typeField, typeQuery));
            throw new WebApplicationException(response);
        }
    }
    
    public String getValue() {
        return value;
    }
    
    public String getTypeField() {
        return typeField;
    }
    
    public String getTypeQuery() {
        return typeQuery;
    }
}
