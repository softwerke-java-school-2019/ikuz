package ru.softwerke.practice.app2019.util;

import javafx.scene.paint.Color;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ColorParam {
    private static final String FORMAT = "the web-color format representation";
    private final Color color;
    private final String typeQuery;
    
    public ColorParam(String colorStr, String typeField, String typeQuery) throws WebApplicationException {
        QueryUtils.checkLengthOfHeaderFields(colorStr, typeField, typeQuery);
        if (StringUtils.isBlank(colorStr)) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.EMPTY_VALUE_TYPE_ERROR,
                            QueryUtils.getEmptyOrNullParamsMessage(typeField, typeQuery));
            throw new WebApplicationException(response);
        }
        try {
            this.color = Color.web(colorStr);
            this.typeQuery = typeQuery;
        } catch (IllegalArgumentException e) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                            QueryUtils.getInvalidFormatMessage(colorStr, typeField, typeQuery, FORMAT));
            throw new WebApplicationException(response);
        }
    }
    
    public Color getColor() {
        return color;
    }
    
    public String getTypeQuery() {
        return typeQuery;
    }
}
