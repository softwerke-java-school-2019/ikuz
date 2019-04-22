package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class QueryUtils {
    private static final int MAX_URI_LENGTH = 500;
    private static final int MAX_QUERY_FIELD_LENGTH = 50;
    private static final int MAX_COUNT = 100;
    public static final String ADD_ENTITY = "add entity";
    public static final String FILTER = "filter";
    public static final String ORDER = "order";
    
    public static long checkCountValue(long count) {
        if (count > MAX_COUNT) {
            return MAX_COUNT;
        } else {
            return count;
        }
    }
    
    public static void checkLengthQuery(int length) throws WebApplicationException {
        if (length > MAX_URI_LENGTH) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.REQUEST_URI_TOO_LONG,
                            "too large length of URI",
                            "given URI has too large length");
            throw new WebApplicationException(response);
        }
    }
    
    static void checkLengthOfHeaderFields(String value, String param, String typeQuery) {
        if (StringUtils.length(value) > MAX_QUERY_FIELD_LENGTH) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.REQUEST_HEADER_FIELDS_TOO_LARGE,
                            "too large length of field query",
                            String.format("the value %s of %s parameter in %s query has too large length",
                                    value, param, typeQuery));
            throw new WebApplicationException(response);
        }
    }
    
    public static Response getResponseWithMessage(Response.Status status, String type, String description) {
        return Response
                .status(status)
                .entity(JSONErrorMessage.create(status, type, description))
                .build();
    }
}
