package ru.softwerke.practice.app2019.util;

import javafx.scene.paint.Color;
import org.apache.commons.lang3.StringUtils;
import ru.softwerke.practice.app2019.query.Query;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ColorParam {
    private static final String COLOR = "color";
    private final Color color;
    private final String typeQuery;
    
    public ColorParam(String colorStr, String typeQuery) throws WebApplicationException {
        QueryUtils.checkLengthOfHeaderFields(colorStr, COLOR, typeQuery);
        if (StringUtils.isBlank(colorStr)) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "empty value of query parameters",
                            String.format("the value of %s parameter in %s query is null or empty", COLOR, typeQuery));
            throw new WebApplicationException(response);
        }
        try {
            this.color = Color.web(colorStr);
            this.typeQuery = typeQuery;
        } catch (IllegalArgumentException e) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "invalid query parameter format",
                            String.format("the value %s of %s parameter in %s query does not match" +
                                    " the web format representation", colorStr, COLOR, typeQuery));
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
