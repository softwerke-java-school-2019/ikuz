package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;
import ru.softwerke.practice.app2019.query.Query;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateParam {
    private static final String DATE = "date";
    private final LocalDate date;
    private final String typeQuery;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(Locale.getDefault());
    public DateParam(String dateStr, String typeQuery) throws WebApplicationException {
        QueryUtils.checkLengthOfHeaderFields(dateStr, DATE, typeQuery);
        if (StringUtils.isBlank(dateStr)) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "empty value of query parameters",
                            String.format("the value of %s parameter in %s query is null or empty", DATE, typeQuery));
            throw new WebApplicationException(response);
        }
        
        try {
            this.date = LocalDate.parse(dateStr, formatter);
            this.typeQuery = typeQuery;
        } catch (DateTimeParseException e) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "invalid query parameter format",
                            String.format("the value %s of %s parameter in %s query does not match the pattern: dd.MM.yyyy",
                                    dateStr, DATE, typeQuery));
            throw new WebApplicationException(response);
        }
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public String getTypeQuery() {
        return typeQuery;
    }
}
