package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;
import ru.softwerke.practice.app2019.query.Query;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class LongParam {
    private final long longValue;
    private final String typeField;
    private final String typeQuery;
    
    public LongParam(String longValueStr, String typeField, String typeQuery) throws WebApplicationException {
        QueryUtils.checkLengthOfHeaderFields(longValueStr, typeField, typeQuery);
        if (StringUtils.isBlank(longValueStr)) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "empty value of query parameters",
                            String.format("the value of %s parameter in %s query is null or empty",
                                    typeField, typeQuery));
            throw new WebApplicationException(response);
        }
        try {
            if (Long.valueOf(longValueStr) >= 0) {
                this.longValue = Long.valueOf(longValueStr);
                this.typeField = typeField;
                this.typeQuery = typeQuery;
            } else {
                Response response = QueryUtils.
                        getResponseWithMessage(Response.Status.BAD_REQUEST,
                                "invalid query parameter format",
                                String.format("the value %s of %s parameter in %s query is negative",
                                        longValueStr, typeField, typeQuery));
                throw new WebApplicationException(response);
            }
        } catch (NumberFormatException e) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "invalid query parameter format",
                            String.format("the value %s of %s parameter in %s query is does not match the format of long number",
                                    longValueStr, typeField, typeQuery));
            throw new WebApplicationException(response);
        }
        
    }
    
    public long getLongValue() {
        return longValue;
    }
    
    public String getTypeField() {
        return typeField;
    }
    
    public String getTypeQuery() {
        return typeQuery;
    }
}
